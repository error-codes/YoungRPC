package io.young.test.provider.service.impl;

import io.young.rpc.annotation.YoungService;
import io.young.test.api.DemoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author YoungCR
 * @date 2024/12/23 9:50
 * @descritpion DemoServiceImpl
 */
@YoungService(interfaceClass = DemoService.class, interfaceClassName = "io.young.rpc.test.api.DemoServer", version = "1.0.0", group = "young")
public class DemoServiceImpl implements DemoService{

    private static final Logger logger = LoggerFactory.getLogger(DemoServiceImpl.class);

    @Override
    public String hello(String name) {
        logger.info("调用hello方法传入的参数为==>{}", name);
        return "hello" + name;
    }
}
