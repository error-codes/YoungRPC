package io.young.test.scanner.service.impl;

import io.young.rpc.annotation.YoungService;
import io.young.test.scanner.service.DemoService;

/**
 * @author YoungCR
 * @date 2024/12/17 14:01
 * @descritpion DemoServiceImpl
 */
@YoungService(interfaceClass = DemoService.class, interfaceClassName = "io.young.rpc.test.scanner.service.DemoService", version = "1.0.0", group = "young")
public class DemoServiceImpl implements DemoService {


}
