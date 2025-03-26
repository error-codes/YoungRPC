package io.young.test.spi.service.impl;

import io.young.rpc.spi.annotation.SPIClass;
import io.young.test.spi.service.SPIService;

/**
 * @author YoungCR
 * @date 2025/3/26 18:37
 * @descritpion SPIServiceImpl
 */
@SPIClass
public class SPIServiceImpl implements SPIService {

    @Override
    public String hello(String name) {
        return "hello " + name;
    }
}
