package io.young.rpc.test.scanner;


import io.young.rpc.common.scanner.ClassScanner;
import io.young.rpc.common.scanner.reference.YoungReferenceScanner;
import io.young.rpc.common.scanner.server.YoungServiceScanner;
import org.junit.Test;

import java.util.Set;

/**
 * @author YoungCR
 * @date 2024/12/17 14:13
 * @descritpion ScannerTest
 */
public class ScannerTest {

    /**
     * 扫描 io.young.rpc.test.scanner 包下的所有类
     */
    @Test
    public void testScannerClassNameList() {
        Set<String> classNameList = ClassScanner.getClassNameList("io.young.rpc.test.scanner");
        classNameList.forEach(System.out::println);
    }

    @Test
    public void testScannerClassNameListByService() {
        YoungServiceScanner.doScannerWithRpcServiceAnnotationFilterAndRegistryService("io.young.rpc.test.scanner");
    }

    @Test
    public void testScannerClassNameListByReference() {
        YoungReferenceScanner.doScannerWithRpcReferenceAnnotationFilter("io.young.rpc.test.scanner");
    }

}
