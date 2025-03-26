package io.young.rpc.consumer;

import io.young.rpc.common.exception.YoungAssert;
import io.young.rpc.common.util.RespUtils;
import io.young.rpc.consumer.common.YoungConsumer;
import io.young.rpc.proxy.api.ProxyFactory;
import io.young.rpc.proxy.api.async.IAsyncObjectProxy;
import io.young.rpc.proxy.api.config.ProxyConfig;
import io.young.rpc.proxy.api.object.ObjectProxy;
import io.young.rpc.proxy.jdk.JdkProxyFactory;
import io.young.rpc.registry.api.RegistryService;
import io.young.rpc.registry.api.config.RegistryConfig;
import io.young.rpc.registry.zookeeper.ZookeeperRegistryService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author YoungCR
 * @date 2025/2/7 18:33
 * @descritpion YoungClient
 */
public class YoungClient {

    private static final Logger logger = LoggerFactory.getLogger(YoungClient.class);

    /**
     * 服务版本号
     */
    private final String version;

    /**
     * 服务分组
     */
    private final String group;

    /**
     * 超时时间，默认 15 s
     */
    private Long timeout = 15000L;

    /**
     * 序列化类型
     */
    private final String serializationType;

    /**
     * 是否异步调用
     */
    private final boolean async;

    /**
     * 是否单向调用
     */
    private final boolean oneway;

    /**
     * 注册中心
     */
    private final RegistryService registryService;

    public YoungClient(String registryAddress, String registryType, String version, String group, Long timeout, String serializationType, boolean async, boolean oneway) {
        this.registryService = getRegistryService(registryAddress, registryType);
        this.version = version;
        this.group = group;
        this.timeout = timeout;
        this.serializationType = serializationType;
        this.async = async;
        this.oneway = oneway;
    }

    public <T> T create(Class<T> clazz) {
        ProxyFactory factory = new JdkProxyFactory();
        factory.initProxy(new ProxyConfig<>(registryService, clazz, version, group, timeout, YoungConsumer.getInstance(), serializationType, async, oneway));
        return factory.getProxy(clazz);
    }

    public <T> IAsyncObjectProxy createAsync(Class<T> clazz) {
        return new ObjectProxy<>(registryService, clazz, version, group, timeout, YoungConsumer.getInstance(), serializationType, async, oneway);
    }

    public void shutdown() {
        YoungConsumer.getInstance().close();
    }

    private RegistryService getRegistryService(String registryAddress, String registryType) {
        YoungAssert.throwAssertIfBlank(registryAddress, RespUtils.getContent("Null.3"));
        YoungAssert.throwAssertIfBlank(registryType, RespUtils.getContent("Null.4"));

        // 后续SPI扩展
        RegistryService service = new ZookeeperRegistryService();
        try {
            service.init(new RegistryConfig(registryAddress, registryType));
        } catch (Exception e) {
            logger.error("YoungClient init registry service error", e);
        } return service;

    }
}
