package io.young.rpc.spi.factory;

import io.young.rpc.spi.annotation.SPI;

/**
 * @author YoungCR
 * @date 2025/3/26 13:38
 * @descritpion ExtensionFactory
 */
@SPI
public interface ExtensionFactory {


    /**
     * 获取扩展
     *
     * @param key key
     * @param type class类型对象
     * @param <T> 泛型
     * @return 扩展类对象
     */
    <T> T getExtension(String key, Class<T> type);
}
