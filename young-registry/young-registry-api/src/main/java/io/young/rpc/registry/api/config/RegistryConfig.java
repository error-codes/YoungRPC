package io.young.rpc.registry.api.config;

import java.io.Serial;
import java.io.Serializable;

/**
 * @author YoungCR
 * @date 2025/3/25 13:05
 * @descritpion RegistryConfig 注册中心配置
 */
public class RegistryConfig implements Serializable {

    @Serial
    private static final long serialVersionUID = -8687494308424365954L;

    /**
     * 注册中心地址
     */
    private String registryAddress;

    /**
     * 注册中心类型
     */
    private String registryType;

    public RegistryConfig(String registryAddress, String registryType) {
        this.registryAddress = registryAddress;
        this.registryType = registryType;
    }

    public String getRegistryAddress() {
        return registryAddress;
    }

    public String getRegistryType() {
        return registryType;
    }
}
