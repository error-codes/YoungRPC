package io.young.rpc.common.scanner.reference;

import io.young.rpc.annotation.YoungReference;
import io.young.rpc.common.exception.ExceptionFactory;
import io.young.rpc.common.exception.WrongArgumentException;
import io.young.rpc.common.scanner.ClassScanner;
import io.young.rpc.common.util.RespUtils;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * @author YoungCR
 * @date 2024/12/17 10:57
 * @descritpion YoungReferenceScanner @YoungReference 注解扫描器
 */
public class YoungReferenceScanner extends ClassScanner {

    public static Map<String, Object> doScannerWithRpcReferenceAnnotationFilter(String scanPackage) {

        Map<String, Object> handlerMap = new HashMap<>();

        Set<String> classNameList = getClassNameList(scanPackage);
        classNameList.forEach(className -> {
            try {
                Class<?> clazz          = Class.forName(className);
                Field[]  declaredFields = clazz.getDeclaredFields();
                Arrays.stream(declaredFields).forEach(field -> {
                    YoungReference reference = field.getAnnotation(YoungReference.class);
                    if (reference != null) {
                        //todo: 待实现
                        System.out.println(reference.async());
                        System.out.println(reference.group());
                        System.out.println(reference.loadBalance());
                        System.out.println(reference.oneway());
                        System.out.println(reference.proxy());
                        System.out.println(reference.registryAddress());
                        System.out.println(reference.registryType());
                        System.out.println(reference.serializationType());
                        System.out.println(reference.timeout());
                        System.out.println(reference.version());
                    }
                });
            } catch (ClassNotFoundException e) {
                throw ExceptionFactory.createException(WrongArgumentException.class, RespUtils.getContent("Util.FailLoadClass", className), e);
            }
        });
        return handlerMap;
    }
}
