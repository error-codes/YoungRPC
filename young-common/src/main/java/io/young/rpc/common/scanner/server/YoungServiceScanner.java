package io.young.rpc.common.scanner.server;

import io.young.rpc.annotation.YoungService;
import io.young.rpc.common.exception.ExceptionFactory;
import io.young.rpc.common.exception.WrongArgumentException;
import io.young.rpc.common.help.ServiceHelper;
import io.young.rpc.common.scanner.ClassScanner;
import io.young.rpc.common.util.RespUtils;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * @author YoungCR
 * @date 2024/12/16 20:29
 * @descritpion YoungServiceScanner @YoungService 注解扫描器
 */
public class YoungServiceScanner extends ClassScanner {

    public static Map<String, Object> doScannerWithRpcServiceAnnotationFilterAndRegistryService(String scanPackage) {
        Map<String, Object> handlerMap    = new HashMap<>();
        Set<String>         classNameList = getClassNameList(scanPackage);
        if (classNameList.isEmpty()) {
            return handlerMap;
        }

        classNameList.forEach(className -> {
            try {
                Class<?>     clazz        = Class.forName(className);
                YoungService youngService = clazz.getAnnotation(YoungService.class);
                if (youngService != null) {
                    String serviceName = getServiceName(youngService);
                    String key      = ServiceHelper.buildServiceKey(serviceName, youngService.version(), youngService.group());
                    handlerMap.put(key, clazz.getDeclaredConstructor().newInstance());
                }
            } catch (ClassNotFoundException e) {
                throw ExceptionFactory.createException(WrongArgumentException.class, RespUtils.getContent("Util.FailLoadClass", className, e));
            } catch (InvocationTargetException | InstantiationException | IllegalAccessException |
                     NoSuchMethodException e) {
                throw new RuntimeException(e);
            }
        });
        return handlerMap;
    }

    private static String getServiceName(YoungService youngService) {
        Class<?> clazz = youngService.interfaceClass();
        if (clazz == void.class) {
            return youngService.interfaceClassName();
        }
        String serviceName = clazz.getName();
        if (serviceName.trim().isEmpty()) {
            serviceName = youngService.interfaceClassName();
        }
        return serviceName;
    }
}
