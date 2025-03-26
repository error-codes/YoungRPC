package io.young.rpc.registry.api;

import io.young.rpc.protocol.meta.ServiceMeta;
import io.young.rpc.registry.api.config.RegistryConfig;

import java.io.IOException;

/**
 * @author YoungCR
 * @date 2025/3/25 13:07
 * @descritpion RegistryService 注册中心服务
 */
public interface RegistryService {

    /**
     * 注册服务
     *
     * @param serviceMeta 服务元信息
     * @throws Exception 注册异常
     */
    void registry(ServiceMeta serviceMeta) throws Exception;

    /**
     * 取消注册服务
     *
     * @param serviceMeta 服务元信息
     * @throws Exception 取消注册异常
     */
    void unRegistry(ServiceMeta serviceMeta) throws Exception;

    /**
     * 服务发现
     *
     * @param serviceName     服务名称
     * @param invokerHashCode 调用者哈希码
     * @return 服务元信息
     * @throws Exception 服务发现异常
     */
    ServiceMeta discovery(String serviceName, int invokerHashCode) throws Exception;

    /**
     * 销毁
     *
     * @throws IOException IO异常
     */
    void destroy() throws IOException;

    /**
     * 初始化
     *
     * @param config 注册中心配置
     * @throws Exception 初始化异常
     */
    default void init(RegistryConfig config) throws Exception {
    }
}
