package io.young.rpc.provider.common.scanner;

import io.young.rpc.annotation.YoungService;
import io.young.rpc.common.exception.ExceptionFactory;
import io.young.rpc.common.exception.WrongArgumentException;
import io.young.rpc.common.help.ServiceHelper;
import io.young.rpc.common.scanner.ClassScanner;
import io.young.rpc.common.util.RespUtils;
import io.young.rpc.protocol.meta.ServiceMeta;
import io.young.rpc.registry.api.RegistryService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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

    private static final Logger logger = LoggerFactory.getLogger(YoungServiceScanner.class);

    public static Map<String, Object> doScannerWithRpcServiceAnnotationFilterAndRegistryService(String host, int port, String scanPackage, RegistryService registryService) {
        Map<String, Object> handlerMap = new HashMap<>(30);
        Set<String> classNameList = getClassNameList(scanPackage);
        if (classNameList.isEmpty()) {
            return handlerMap;
        }

        classNameList.forEach(className -> {
            try {
                Class<?> clazz = Class.forName(className);
                YoungService youngService = clazz.getAnnotation(YoungService.class);
                if (youngService != null) {
                    // 优先使用interfaceClass，如果没有设置则使用interfaceClassName
                    ServiceMeta serviceMeta = new ServiceMeta(getServiceName(youngService), youngService.version(), host, port, youngService.group());
                    // 注册元数据到注册中心
                    registryService.registry(serviceMeta);
                    handlerMap.put(ServiceHelper.buildServiceKey(serviceMeta.getServiceName(), serviceMeta.getServiceVersion(), serviceMeta.getServiceGroup()), clazz.getDeclaredConstructor().newInstance());
                }
            } catch (ClassNotFoundException e) {
                logger.error(RespUtils.getContent("Util.0", className, e));
            } catch (Exception e) {
                logger.error(RespUtils.getContent("Error.0", e.getMessage(), e));
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
