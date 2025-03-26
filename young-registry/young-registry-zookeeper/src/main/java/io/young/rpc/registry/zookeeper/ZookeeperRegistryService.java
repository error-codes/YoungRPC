package io.young.rpc.registry.zookeeper;

import io.young.rpc.common.exception.YoungAssert;
import io.young.rpc.common.help.ServiceHelper;
import io.young.rpc.common.util.RespUtils;
import io.young.rpc.loadbalancer.RandomServiceLoadBalancer;
import io.young.rpc.loadbalancer.api.ServiceLoadBalancer;
import io.young.rpc.protocol.meta.ServiceMeta;
import io.young.rpc.registry.api.RegistryService;
import io.young.rpc.registry.api.config.RegistryConfig;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.curator.x.discovery.ServiceDiscovery;
import org.apache.curator.x.discovery.ServiceDiscoveryBuilder;
import org.apache.curator.x.discovery.ServiceInstance;
import org.apache.curator.x.discovery.details.JsonInstanceSerializer;

import java.io.IOException;
import java.io.Serial;
import java.io.Serializable;
import java.util.Collection;
import java.util.List;

/**
 * @author YoungCR
 * @date 2025/3/25 13:14
 * @descritpion ZookeeperRegistryService Zookeeper注册中心服务
 */
public class ZookeeperRegistryService implements Serializable, RegistryService {

    @Serial
    private static final long serialVersionUID = 8129956044882680919L;

    public static final int BASE_SLEEP_TIME_MS = 1000;
    public static final int MAX_RETRIES = 3;
    public static final String ZK_ROOT_PATH = "/young-rpc";
    private ServiceDiscovery<ServiceMeta> serviceDiscovery;

    private ServiceLoadBalancer<ServiceInstance<ServiceMeta>> serviceLoadBalancer;

    @Override
    public void init(RegistryConfig config) throws Exception {
        CuratorFramework client = CuratorFrameworkFactory.newClient(config.getRegistryAddress(), new ExponentialBackoffRetry(BASE_SLEEP_TIME_MS, MAX_RETRIES));
        client.start();
        JsonInstanceSerializer<ServiceMeta> serializer = new JsonInstanceSerializer<>(ServiceMeta.class);
        this.serviceLoadBalancer = new RandomServiceLoadBalancer<>();
        this.serviceDiscovery = ServiceDiscoveryBuilder.builder(ServiceMeta.class)
                                                       .client(client)
                                                       .basePath(ZK_ROOT_PATH)
                                                       .serializer(serializer)
                                                       .build();
        this.serviceDiscovery.start();
    }

    @Override
    public void registry(ServiceMeta serviceMeta) throws Exception {
        ServiceInstance<ServiceMeta> serviceInstance = ServiceInstance.<ServiceMeta>builder()
                                                                      .name(ServiceHelper.buildServiceKey(serviceMeta.getServiceName(), serviceMeta.getServiceVersion(), serviceMeta.getServiceGroup()))
                                                                      .address(serviceMeta.getServiceAddress())
                                                                      .port(serviceMeta.getServicePort())
                                                                      .payload(serviceMeta)
                                                                      .build();
        serviceDiscovery.registerService(serviceInstance);
    }

    @Override
    public void unRegistry(ServiceMeta serviceMeta) throws Exception {
        ServiceInstance<ServiceMeta> serviceInstance = ServiceInstance.<ServiceMeta>builder()
                                                                      .name(serviceMeta.getServiceName())
                                                                      .address(serviceMeta.getServiceAddress())
                                                                      .port(serviceMeta.getServicePort())
                                                                      .payload(serviceMeta)
                                                                      .build();
        serviceDiscovery.unregisterService(serviceInstance);
    }

    @Override
    public ServiceMeta discovery(String serviceName, int invokerHashCode) throws Exception {
        Collection<ServiceInstance<ServiceMeta>> serviceInstances = serviceDiscovery.queryForInstances(serviceName);
        ServiceInstance<ServiceMeta> serviceInstance = serviceLoadBalancer.select((List<ServiceInstance<ServiceMeta>>) serviceInstances, invokerHashCode);
        YoungAssert.throwAssertIfNullOrEmpty(serviceInstance, RespUtils.getContent("Null.2"));
        return serviceInstance.getPayload();
    }

    @Override
    public void destroy() throws IOException {
        serviceDiscovery.close();
    }
}
