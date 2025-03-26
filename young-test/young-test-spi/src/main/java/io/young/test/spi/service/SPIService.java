package io.young.test.spi.service;

import io.young.rpc.spi.annotation.SPI;

/**
 * @author YoungCR
 * @date 2025/3/26 18:36
 * @descritpion SPIService
 */
@SPI("spiService")
public interface SPIService {

    String hello(String name);
}
