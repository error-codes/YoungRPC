package io.young.test.spi;

import io.young.rpc.spi.loader.ExtensionLoader;
import io.young.test.spi.service.SPIService;
import org.junit.Test;

/**
 * @author YoungCR
 * @date 2025/3/26 18:40
 * @descritpion SPITest
 */
public class SPITest {

    @Test
    public void testSpiLoader() {
        SPIService spiService = ExtensionLoader.getExtensionLoader(SPIService.class, "spiService");
        String hello = spiService.hello("YoungCR");
        System.out.println(hello);
    }
}
