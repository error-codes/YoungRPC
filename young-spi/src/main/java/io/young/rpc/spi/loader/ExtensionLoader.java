package io.young.rpc.spi.loader;

import io.young.rpc.common.exception.ExceptionFactory;
import io.young.rpc.common.exception.YoungAssert;
import io.young.rpc.common.util.RespUtils;
import io.young.rpc.spi.annotation.SPI;
import io.young.rpc.spi.annotation.SPIClass;
import io.young.rpc.spi.factory.ExtensionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * @author YoungCR
 * @date 2025/3/26 13:44
 * @descritpion ExtensionLoader SPI扩展加载器
 * 用于根据接口加载实现类并管理实例。通过扫描指定目录下的配置文件，加载带有{@link SPIClass}注解的实现类， 支持默认实现和按名称获取扩展实例。
 */
public class ExtensionLoader<T> {

    private static final Logger logger = LoggerFactory.getLogger(ExtensionLoader.class);

    /**
     * 服务提供者接口配置文件的根目录路径
     */
    private static final String SERVICES_DIRECTORY = "META/services";

    /**
     * 项目内部SPI配置目录
     */
    private static final String YOUNG_DIRECTORY = "META/young";

    /**
     * 项目外部SPI配置目录
     */
    private static final String YOUNG_DIRECTORY_EXTERNAL = "META/young/external";

    /**
     * 项目内部专用SPI配置目录
     */
    private static final String YOUNG_DIRECTORY_INTERNAL = "META/young/internal";

    /**
     * 需要扫描的SPI配置目录列表，按优先级排序
     */
    private static final String[] SPI_DIRECTORIES = new String[]{
            SERVICES_DIRECTORY,
            YOUNG_DIRECTORY,
            YOUNG_DIRECTORY_EXTERNAL,
            YOUNG_DIRECTORY_INTERNAL
    };

    /**
     * 缓存所有ExtensionLoader实例的注册表
     */
    private static final Map<Class<?>, ExtensionLoader<?>> LOADERS = new ConcurrentHashMap<>();

    /**
     * 当前加载器对应的扩展接口类型
     */
    private final Class<T> clazz;

    /**
     * 类加载器实例，用于加载类
     */
    private final ClassLoader classLoader;

    /**
     * 延迟加载的扩展类缓存（名称到类的映射）
     */
    private final Holder<Map<String, Class<T>>> cachedClasses = new Holder<>();

    /**
     * 已创建的扩展实例缓存（名称到实例的映射）
     */
    private final Map<String, Holder<T>> cachedInstances = new ConcurrentHashMap<>();

    /**
     * 已加载的SPI类到实例的直接映射（用于快速查找）
     */
    private final Map<Class<?>, T> spiClassInstances = new ConcurrentHashMap<>();

    /**
     * 默认实现的名称（由{@link SPI}注解指定）
     */
    private String cachedDefaultName;

    /**
     * 私有构造函数，通过{@link #getExtensionLoader}获取实例
     *
     * @param clazz       要加载的扩展接口类型
     * @param classLoader 类加载器
     */
    private ExtensionLoader(final Class<T> clazz, final ClassLoader classLoader) {
        this.clazz = clazz;
        this.classLoader = classLoader;
        // 初始化ExtensionFactory的特殊处理（避免循环依赖）
        if (clazz == ExtensionFactory.class) {
            getExtensionLoader(ExtensionFactory.class).getExtensionClasses();
        }
    }

    /**
     * 获取指定接口的ExtensionLoader实例
     *
     * @param <T>         扩展接口类型
     * @param clazz       扩展接口类
     * @param classLoader 类加载器（可为null使用系统类加载器）
     * @return ExtensionLoader实例
     * @throws IllegalArgumentException 如果接口不符合SPI规范
     */
    @SuppressWarnings("unchecked")
    public static <T> ExtensionLoader<T> getExtensionLoader(final Class<?> clazz, final ClassLoader classLoader) {
        YoungAssert.throwAssertIfNullOrEmpty(clazz, RespUtils.getContent("Null.5"));
        YoungAssert.throwAssertIfFalse(clazz.isInterface(), RespUtils.getContent("Null.6", clazz.toString()));
        YoungAssert.throwAssertIfFalse(clazz.isAnnotationPresent(SPI.class), RespUtils.getContent("Null.7", clazz.toString(), SPI.class.toString()));

        return (ExtensionLoader<T>) LOADERS.computeIfAbsent(clazz, key -> new ExtensionLoader<>(key, classLoader));
    }

    /**
     * 便捷方法：通过接口类型获取ExtensionLoader实例（使用当前类的类加载器）
     *
     * @param <T>   扩展接口类型
     * @param clazz 扩展接口类
     * @return ExtensionLoader实例
     */
    public static <T> ExtensionLoader<T> getExtensionLoader(final Class<T> clazz) {
        return getExtensionLoader(clazz, ExtensionLoader.class.getClassLoader());
    }

    /**
     * 便捷方法：获取指定名称的扩展实例（空字符串时获取默认实现）
     *
     * @param <T>   扩展接口类型
     * @param clazz 扩展接口类
     * @param name  实现类的名称（对应配置文件中的键）
     * @return 扩展实例
     * @throws IllegalArgumentException 如果名称无效或未找到实现
     */
    public static <T> T getExtensionLoader(final Class<T> clazz, final String name) {
        return name.isBlank() ? getExtensionLoader(clazz).getDefaultSpiClassInstance() : getExtensionLoader(clazz).getSpiClassInstance(name);
    }

    /**
     * 获取默认实现的实例
     *
     * @return 默认扩展实例
     * @throws IllegalStateException 如果未找到默认实现
     */
    public T getDefaultSpiClassInstance() {
        getExtensionClasses(); // 确保类已加载
        return cachedInstances.get(cachedDefaultName).getValue();
    }

    /**
     * 根据名称获取扩展实例
     *
     * @param name 实现类的名称
     * @return 扩展实例
     * @throws IllegalArgumentException 如果名称无效或未找到实现
     */
    public T getSpiClassInstance(final String name) {
        YoungAssert.throwAssertIfBlank(name, RespUtils.getContent("Null.8"));
        Holder<T> instanceHolder = cachedInstances.computeIfAbsent(name, key -> new Holder<>());
        T instance = instanceHolder.getValue();
        if (instance == null) {
            synchronized (cachedInstances) {
                instance = instanceHolder.getValue();
                if (instance == null) {
                    instance = createExtension(name);
                    instanceHolder.setValue(instance);
                }
            }
        }
        return instance;
    }

    /**
     * 获取所有已加载的扩展实例
     *
     * @return 扩展实例列表
     */
    public List<T> getSpiClassInstances() {
        Map<String, Class<T>> extensionClasses = getExtensionClasses();
        if (extensionClasses.isEmpty()) {
            return Collections.emptyList();
        }

        // 如果缓存实例已全部加载则直接返回
        if (extensionClasses.size() == cachedInstances.size()) {
            return cachedInstances.values().stream().map(Holder::getValue).collect(Collectors.toList());
        }

        // 重新加载所有实例
        List<T> instances = new ArrayList<>();
        extensionClasses.forEach((name, clazz) -> instances.add(getSpiClassInstance(name)));
        return instances;
    }

    /**
     * 根据名称创建扩展实例（线程安全）
     *
     * @param name 实现类的名称
     * @return 扩展实例
     * @throws RuntimeException 如果类加载或实例化失败
     */
    private T createExtension(final String name) {
        Class<T> clazz = getExtensionClasses().get(name);
        YoungAssert.throwAssertIfNullOrEmpty(clazz, RespUtils.getContent("Null.9", clazz.toString()));

        T instance = spiClassInstances.get(clazz);
        if (Objects.isNull(instance)) {

            // 双重检查锁确保线程安全
            try {
                spiClassInstances.putIfAbsent(clazz, clazz.getDeclaredConstructor().newInstance());
                instance = spiClassInstances.get(clazz);
            } catch (ReflectiveOperationException e) {
                throw ExceptionFactory.createException(RespUtils.getContent("Error.2", clazz.getName(), clazz, e.getMessage()), e);
            }
        }
        return instance;
    }

    /**
     * 获取所有已加载的扩展类（名称到类的映射）
     *
     * @return 扩展类映射
     */
    public Map<String, Class<T>> getExtensionClasses() {
        Map<String, Class<T>> classes = cachedClasses.getValue();
        if (Objects.isNull(classes)) {
            synchronized (cachedClasses) {
                if ((classes = cachedClasses.getValue()) == null) {
                    classes = loadExtensionClass();
                    cachedClasses.setValue(classes);
                }
            }
        }
        return classes;
    }

    /**
     * 加载所有扩展类（延迟初始化）
     *
     * @return 扩展类映射
     */
    private Map<String, Class<T>> loadExtensionClass() {
        // 读取SPI注解的默认值
        SPI spiAnnotation = clazz.getAnnotation(SPI.class);
        if (spiAnnotation != null) {
            String defaultName = spiAnnotation.value().trim();
            if (!defaultName.isEmpty()) {
                cachedDefaultName = defaultName;
            }
        }

        Map<String, Class<T>> classes = new HashMap<>(16);
        // 依次扫描所有配置目录
        loadDirectory(classes);
        return classes;
    }

    /**
     * 加载指定目录下的所有配置文件
     *
     * @param classes 存储加载结果的映射
     */
    private void loadDirectory(final Map<String, Class<T>> classes) {
        for (String directory : SPI_DIRECTORIES) {
            String fileName = directory + "/" + clazz.getName();
            try {
                Enumeration<URL> urls = classLoader != null
                        ? classLoader.getResources(fileName)
                        : ClassLoader.getSystemResources(fileName);

                while (urls.hasMoreElements()) {
                    URL url = urls.nextElement();
                    loadResources(classes, url);
                }
            } catch (IOException e) {
                logger.error(RespUtils.getContent("File.3", fileName));
            }
        }
    }

    /**
     * 加载单个配置文件中的类定义
     *
     * @param classes 配置项存储的映射
     * @param url     配置文件URL
     */
    private void loadResources(final Map<String, Class<T>> classes, final URL url) {
        try (InputStream in = url.openStream()) {
            Properties properties = new Properties();
            properties.load(in);
            properties.forEach((key, value) -> {
                String name = key.toString().trim();
                String className = value.toString().trim();

                if (!name.isEmpty() && !className.isEmpty()) {
                    try {
                        loadClass(classes, name, className);
                    } catch (ClassNotFoundException e) {
                        logger.error(RespUtils.getContent("File.3", name), e);
                    }
                }
            });
        } catch (IOException e) {
            logger.error(RespUtils.getContent("File.3", url), e);
        }
    }

    /**
     * 将类名加载为Class对象并验证
     *
     * @param classes   类映射存储
     * @param name      扩展名称
     * @param className 类全限定名
     * @throws ClassNotFoundException 如果类不存在
     */
    @SuppressWarnings("unchecked")
    private void loadClass(final Map<String, Class<T>> classes, final String name, final String className)
            throws ClassNotFoundException {
        Class<?> subClass = classLoader != null
                ? Class.forName(className, true, classLoader)
                : Class.forName(className);

        // 验证是否为接口的子类
        YoungAssert.throwAssertIfFalse(clazz.isAssignableFrom(subClass),
                RespUtils.getContent("File.4", subClass, clazz));

        // 验证是否带有SPIClass注解
        YoungAssert.throwAssertIfFalse(subClass.isAnnotationPresent(SPIClass.class),
                RespUtils.getContent("File.5", subClass, SPIClass.class));

        // 检查名称冲突
        Class<?> existing = classes.putIfAbsent(name, (Class<T>) subClass);
        if (existing != null && !existing.equals(subClass)) {
            YoungAssert.throwAssertIfNotEqual(existing, subClass,
                    RespUtils.getContent("File.6", clazz.getName(), name, existing.getName()));
        }
    }

    /**
     * 线程安全的值持有器，用于延迟初始化和缓存
     *
     * @param <T> 持有值的类型
     */
    public static class Holder<T> {
        private volatile T value;

        /**
         * 获取当前值（可能为null表示未初始化）
         *
         * @return 值
         */
        public T getValue() {
            return value;
        }

        /**
         * 设置新值（线程安全）
         *
         * @param value 新值
         */
        public void setValue(T value) {
            this.value = value;
        }
    }
}