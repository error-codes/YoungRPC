package io.young.rpc.common.scanner;

import io.young.rpc.common.exception.YoungAssert;
import io.young.rpc.common.util.RespUtils;

import java.io.File;
import java.io.IOException;
import java.net.JarURLConnection;
import java.net.URL;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.jar.JarFile;

/**
 * @author YoungCR
 * @date 2024/12/16 10:40
 * @descritpion ClassScanner 类扫描器
 */
public class ClassScanner {

    /**
     * 文件
     */
    private static final String PROTOCOL_FILE = "file";

    /**
     * jar包
     */
    private static final String PROTOCOL_JAR = "jar";

    /**
     * class文件后缀
     */
    private static final String CLASS_FILE_SUFFIX = ".class";


    /**
     * 通过文件扫描当前工程目录下所有的类信息
     *
     * @param packageName  包名称
     * @param packagePath  包的完整目录
     * @param recursive    是否递归调用
     * @param classNameSet 类名称集合
     */
    private static void scanClassesFromFile(String packageName, String packagePath, boolean recursive, Set<String> classNameSet) {
        File dir = new File(packagePath);
        if (!dir.exists() || !dir.isDirectory()) {
            throw new RuntimeException(RespUtils.getContent("File.0"));
        }

        Optional.ofNullable(dir.listFiles()).stream().flatMap(Arrays::stream).filter(file -> (recursive && file.isDirectory()) || file.getName().endsWith(CLASS_FILE_SUFFIX)).forEach(file -> {
            if (file.isDirectory()) {
                scanClassesFromFile(packageName + "." + file.getName(), file.getAbsolutePath(), recursive, classNameSet);
            } else {
                classNameSet.add(packageName + "." + file.getName().replace(CLASS_FILE_SUFFIX, ""));
            }
        });
    }


    /**
     * 通过文件扫描当前工程目录下所有的类信息
     *
     * @param packageName    包名称
     * @param packageDirName 当前包名的前部分名称
     * @param url            包的url地址
     * @param recursive      是否递归调用
     * @param classNameSet   类名称集合
     */
    private static void scanClassesFromJar(String packageName, String packageDirName, URL url, boolean recursive, Set<String> classNameSet) {

        try (JarFile jarFile = ((JarURLConnection) url.openConnection()).getJarFile()) {
            jarFile.stream()
                    .filter(entry -> !entry.isDirectory() &&
                            entry.getName().startsWith(packageDirName) &&
                            entry.getName().endsWith(CLASS_FILE_SUFFIX))
                    .forEach(entry -> {
                        String name      = entry.getName().replace('/', '.');
                        String className = name.substring(0, name.length() - CLASS_FILE_SUFFIX.length());
                        classNameSet.add(className);
                    });
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    /**
     * 扫描指定包下的所有类名
     *
     * @param packageName 包名
     * @return 类名列表
     */
    public static Set<String> getClassNameList(String packageName) {

        YoungAssert.throwAssertIfBlank(packageName, RespUtils.getContent("Null.0", packageName));

        Set<String> classNameSet   = new HashSet<>();
        String      packageDirName = packageName.replace('.', '/');

        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        try {
            Enumeration<URL> dirs = classLoader.getResources(packageDirName);

            while (dirs.hasMoreElements()) {
                URL    url      = dirs.nextElement();
                String protocol = url.getProtocol();

                switch (protocol) {
                    case PROTOCOL_FILE:
                        String filePath = URLDecoder.decode(url.getFile(), StandardCharsets.UTF_8);
                        scanClassesFromFile(packageName, filePath, true, classNameSet);
                        break;
                    case PROTOCOL_JAR:
                        scanClassesFromJar(packageName, packageDirName, url, true, classNameSet);
                        break;
                    default:
                        RespUtils.getContent("Support.0", protocol);
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return classNameSet;
    }
}
