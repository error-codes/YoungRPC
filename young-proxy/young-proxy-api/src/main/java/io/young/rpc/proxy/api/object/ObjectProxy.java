package io.young.rpc.proxy.api.object;

import io.young.rpc.common.exception.ExceptionFactory;
import io.young.rpc.common.exception.WrongArgumentException;
import io.young.rpc.common.util.RespUtils;
import io.young.rpc.protocol.YoungProtocol;
import io.young.rpc.protocol.header.YoungHeaderFactory;
import io.young.rpc.protocol.request.YoungRequest;
import io.young.rpc.proxy.api.async.IAsyncObjectProxy;
import io.young.rpc.proxy.api.consumer.Consumer;
import io.young.rpc.proxy.api.future.YoungFuture;
import io.young.rpc.registry.api.RegistryService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.concurrent.TimeUnit;

/**
 * @author YoungCR
 * @date 2025/2/7 17:30
 * @descritpion ObjectProxy 代理对象
 */
public class ObjectProxy<T> implements InvocationHandler, IAsyncObjectProxy {

    private static final Logger logger = LoggerFactory.getLogger(ObjectProxy.class);

    /**
     * 接口的 Class 对象
     */
    private final Class<T> clazz;

    /**
     * 服务版本号
     */
    private final String version;

    /**
     * 服务分组
     */
    private final String group;

    /**
     * 超时时间，默认 15 s
     */
    private Long timeout = 15000L;

    /**
     * 服务消费者
     */
    private final Consumer consumer;

    /**
     * 序列化类型
     */
    private final String serializationType;

    /**
     * 是否异步调用
     */
    private final boolean async;

    /**
     * 是否单向调用
     */
    private final boolean oneway;

    /**
     * 注册中心服务
     */
    private final RegistryService registryService;

    public ObjectProxy(RegistryService registryService, Class<T> clazz, String version, String group, Long timeout, Consumer consumer, String serializationType, boolean async, boolean oneway) {
        this.registryService = registryService;
        this.clazz = clazz;
        this.version = version;
        this.group = group;
        this.timeout = timeout;
        this.consumer = consumer;
        this.serializationType = serializationType;
        this.async = async;
        this.oneway = oneway;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        if (Object.class == method.getDeclaringClass()) {
            String methodName = method.getName();
            return switch (methodName) {
                case "equals" -> proxy == args[0];
                case "hashCode" -> System.identityHashCode(proxy);
                case "toString" -> proxy.getClass().getName() + "@" + Integer.toHexString(System.identityHashCode(proxy)) + ", with InvocationHandler " + this;
                default -> throw ExceptionFactory.createException(IllegalStateException.class, String.valueOf(method));
            };
        }

        YoungProtocol<YoungRequest> requestYoungProtocol = new YoungProtocol<>();
        requestYoungProtocol.setHeader(YoungHeaderFactory.createYoungHeader(serializationType));
        YoungRequest request = new YoungRequest();
        request.version(this.version)
               .className(method.getDeclaringClass().getName())
               .methodName(method.getName())
               .parameterTypes(method.getParameterTypes())
               .group(this.group)
               .parameters(args)
               .async(this.async)
               .oneway(this.oneway);
        requestYoungProtocol.setBody(request);

        logger.debug(method.getDeclaringClass().getName());
        logger.debug(method.getName());

        for (Class<?> parameterType : method.getParameterTypes()) {
            logger.debug(parameterType.getName());
        }

        if (args != null) {
            for (Object arg : args) {
                logger.debug(arg.toString());
            }
        }

        YoungFuture youngFuture = this.consumer.sendRequest(requestYoungProtocol, registryService);
        return youngFuture == null ? null : timeout > 0 ? youngFuture.get(timeout, TimeUnit.MILLISECONDS) : youngFuture.get();
    }

    @Override
    public YoungFuture call(String funcName, Object... args) {
        YoungProtocol<YoungRequest> request = createRequest(this.clazz.getName(), funcName, args);
        YoungFuture future = null;
        try {
            future = this.consumer.sendRequest(request, registryService);
        } catch (Exception e) {
            logger.error("async all throws exception: {}", e.getMessage());
        }
        return future;
    }

    private YoungProtocol<YoungRequest> createRequest(String className, String methodName, Object... args) {
        YoungProtocol<YoungRequest> requestYoungProtocol = new YoungProtocol<>();
        requestYoungProtocol.setHeader(YoungHeaderFactory.createYoungHeader(serializationType));
        YoungRequest request = new YoungRequest();
        request.version(this.version)
               .className(className)
               .methodName(methodName)
               .parameters(args)
               .group(this.group);

        Class<?>[] parameterTypes = new Class[args.length];
        for (int i = 0; i < args.length; i++) {
            parameterTypes[i] = getClassType(args[i]);
        }
        request.parameterTypes(parameterTypes);
        requestYoungProtocol.setBody(request);

        logger.debug(className);
        logger.debug(methodName);
        for (Class<?> type : parameterTypes) {
            logger.debug(type.getName());
        }

        for (Object arg : args) {
            logger.debug(arg.toString());
        }

        return requestYoungProtocol;
    }

    private Class<?> getClassType(Object arg) {
        Class<?> classType = arg.getClass();
        switch (classType.getName()) {
            case "java.lang.Byte" -> classType = Byte.TYPE;
            case "java.lang.Short" -> classType = Short.TYPE;
            case "java.lang.Integer" -> classType = Integer.TYPE;
            case "java.lang.Long" -> classType = Long.TYPE;
            case "java.lang.Float" -> classType = Float.TYPE;
            case "java.lang.Double" -> classType = Double.TYPE;
            case "java.lang.Character" -> classType = Character.TYPE;
            case "java.lang.Boolean" -> classType = Boolean.TYPE;
            default -> ExceptionFactory.createException(WrongArgumentException.class, RespUtils.getContent("Support.1", classType.getName()));
        }
        return classType;
    }
}
