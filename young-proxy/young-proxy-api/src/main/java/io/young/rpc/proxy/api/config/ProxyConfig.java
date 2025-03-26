package io.young.rpc.proxy.api.config;

import io.young.rpc.proxy.api.consumer.Consumer;
import io.young.rpc.registry.api.RegistryService;

import java.io.Serial;
import java.io.Serializable;

/**
 * @author YoungCR
 * @date 2025/3/25 10:52
 * @descritpion ProxyConfig
 */
public class ProxyConfig<T> implements Serializable {

    @Serial
    private static final long serialVersionUID = -4183724245594312600L;

    /**
     * 代理类型
     */
    private Class<T> clazz;

    /**
     * 服务版本
     */
    private String serviceVersion;

    /**
     * 服务分组
     */
    private String serviceGroup;

    /**
     * 超时时间
     */
    private Long timeout;

    /**
     * 消费者接口
     */
    private Consumer consumer;

    /**
     * 序列化类型
     */
    private String serializationType;

    /**
     * 是否异步
     */
    private Boolean async;

    /**
     * 是否单向
     */
    private Boolean oneway;

    /**
     * 服务注册
     */
    private RegistryService registryService;

    public ProxyConfig(RegistryService registryService, Class<T> clazz, String serviceVersion, String serviceGroup, Long timeout, Consumer consumer, String serializationType, Boolean async, Boolean oneway) {
        this.registryService = registryService;
        this.clazz = clazz;
        this.serviceVersion = serviceVersion;
        this.serviceGroup = serviceGroup;
        this.timeout = timeout;
        this.consumer = consumer;
        this.serializationType = serializationType;
        this.async = async;
        this.oneway = oneway;
    }

    public RegistryService getRegistryService() {
        return registryService;
    }

    public ProxyConfig<T> registryService(RegistryService registryService) {
        this.registryService = registryService;
        return this;
    }

    public Class<T> getClazz() {
        return clazz;
    }

    public ProxyConfig<T> clazz(Class<T> clazz) {
        this.clazz = clazz;
        return this;
    }

    public String getServiceVersion() {
        return serviceVersion;
    }

    public ProxyConfig<T> serviceVersion(String serviceVersion) {
        this.serviceVersion = serviceVersion;
        return this;
    }

    public String getServiceGroup() {
        return serviceGroup;
    }

    public ProxyConfig<T> serviceGroup(String serviceGroup) {
        this.serviceGroup = serviceGroup;
        return this;
    }

    public Long getTimeout() {
        return timeout;
    }

    public ProxyConfig<T> timeout(Long timeout) {
        this.timeout = timeout;
        return this;
    }

    public Consumer getConsumer() {
        return consumer;
    }

    public ProxyConfig<T> consumer(Consumer consumer) {
        this.consumer = consumer;
        return this;
    }

    public String getSerializationType() {
        return serializationType;
    }

    public ProxyConfig<T> serializationType(String serializationType) {
        this.serializationType = serializationType;
        return this;
    }

    public Boolean getAsync() {
        return async;
    }

    public ProxyConfig<T> async(Boolean async) {
        this.async = async;
        return this;
    }

    public Boolean getOneway() {
        return oneway;
    }

    public ProxyConfig<T> oneway(Boolean oneway) {
        this.oneway = oneway;
        return this;
    }
}
