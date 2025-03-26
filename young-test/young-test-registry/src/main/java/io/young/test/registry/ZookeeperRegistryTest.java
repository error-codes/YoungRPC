package io.young.test.registry;

import io.young.rpc.protocol.meta.ServiceMeta;
import io.young.rpc.registry.api.RegistryService;
import io.young.rpc.registry.api.config.RegistryConfig;
import io.young.rpc.registry.zookeeper.ZookeeperRegistryService;
import org.junit.Before;
import org.junit.Test;

/**
 * @author YoungCR
 * @date 2025/3/25 14:50
 * @descritpion ZookeeperRegistryTest
 */
public class ZookeeperRegistryTest {

    private RegistryService registryService;
    private ServiceMeta serviceMeta;

    @Before
    public void init() throws Exception {
        RegistryConfig config = new RegistryConfig("127.0.0.1:2181", "zookeeper");
        this.registryService = new ZookeeperRegistryService();
        this.registryService.init(config);
        this.serviceMeta = new ServiceMeta(ZookeeperRegistryTest.class.getName(), "1.0.0", "127.0.0.1", 27880, "young");
    }

    @Test
    public void testRegister() throws Exception {
        registryService.registry(serviceMeta);
    }

    @Test
    public void testUnRegister() throws Exception {
        registryService.unRegistry(serviceMeta);
    }

    @Test
    public void testDestroy() throws Exception {
        registryService.destroy();
    }

    @Test
    public void testDiscovery() throws Exception {
        ServiceMeta serviceMeta = registryService.discovery(ZookeeperRegistryTest.class.getName(), "1.0.0", "young");
        System.out.println(serviceMeta);
    }
}
