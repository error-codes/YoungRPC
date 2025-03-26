package io.young.rpc.proxy.api;

import io.young.rpc.proxy.api.config.ProxyConfig;
import io.young.rpc.proxy.api.object.ObjectProxy;

/**
 * @author YoungCR
 * @date 2025/3/25 11:04
 * @descritpion BaseProxyFactory
 */
public abstract class BaseProxyFactory implements ProxyFactory {

    protected ObjectProxy<?> proxy;

    @Override
    public <T> void initProxy(ProxyConfig<T> config) {
        this.proxy = new ObjectProxy<>(
                config.getRegistryService(),
                config.getClazz(),
                config.getServiceVersion(),
                config.getServiceGroup(),
                config.getTimeout(),
                config.getConsumer(),
                config.getSerializationType(),
                config.getAsync(),
                config.getOneway());
    }
}
