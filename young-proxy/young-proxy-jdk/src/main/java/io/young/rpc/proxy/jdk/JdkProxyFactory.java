package io.young.rpc.proxy.jdk;

import io.young.rpc.proxy.api.BaseProxyFactory;
import io.young.rpc.proxy.api.ProxyFactory;

import java.lang.reflect.Proxy;

/**
 * @author YoungCR
 * @date 2025/2/7 17:57
 * @descritpion JdkProxyFactory JDK代理工厂
 */
public class JdkProxyFactory extends BaseProxyFactory implements ProxyFactory {

    public JdkProxyFactory() {
    }

    @Override
    public <T> T getProxy(Class<T> clazz) {
        return clazz.cast(Proxy.newProxyInstance(clazz.getClassLoader(), new Class<?>[]{clazz}, proxy));
    }
}
