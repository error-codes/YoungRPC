package io.young.rpc.proxy.api;

import io.young.rpc.proxy.api.config.ProxyConfig;

/**
 * @author YoungCR
 * @date 2025/3/25 11:02
 * @descritpion ProxyFactory
 */
public interface ProxyFactory {

    /**
     * 获取代理
     *
     * @param clazz 代理类
     * @return 代理对象
     */
    <T> T getProxy(Class<T> clazz);

    /**
     * 初始化代理
     *
     * @param config 代理配置
     */
    default <T> void initProxy(ProxyConfig<T> config) {}
}
