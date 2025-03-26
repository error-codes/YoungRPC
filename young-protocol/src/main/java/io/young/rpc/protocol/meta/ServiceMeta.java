package io.young.rpc.protocol.meta;

import java.io.Serial;
import java.io.Serializable;

/**
 * @author YoungCR
 * @date 2025/3/25 13:01
 * @descritpion ServiceMeta
 */
public class ServiceMeta implements Serializable {

    @Serial
    private static final long serialVersionUID = -1400130112611165573L;

    /**
     * 服务名称
     */
    private String serviceName;
    /**
     * 服务版本
     */
    private String serviceVersion;
    /**
     * 服务地址
     */
    private String serviceAddress;
    /**
     * 服务端口
     */
    private Integer servicePort;
    /**
     * 服务分组
     */
    private String serviceGroup;

    public ServiceMeta() {
    }

    public ServiceMeta(String serviceName, String serviceVersion, String serviceAddress, Integer servicePort, String serviceGroup) {
        this.serviceName = serviceName;
        this.serviceVersion = serviceVersion;
        this.serviceAddress = serviceAddress;
        this.servicePort = servicePort;
        this.serviceGroup = serviceGroup;
    }

    public String getServiceName() {
        return serviceName;
    }

    public String getServiceVersion() {
        return serviceVersion;
    }

    public String getServiceAddress() {
        return serviceAddress;
    }

    public Integer getServicePort() {
        return servicePort;
    }

    public String getServiceGroup() {
        return serviceGroup;
    }
}
