package io.young.test.consumer;

import io.young.rpc.consumer.YoungClient;
import io.young.rpc.proxy.api.async.IAsyncObjectProxy;
import io.young.rpc.proxy.api.future.YoungFuture;
import io.young.test.api.DemoService;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author YoungCR
 * @date 2025/2/7 18:41
 * @descritpion YoungConsumerNativeTest
 */
public class YoungConsumerNativeTest {

    private static final Logger logger = LoggerFactory.getLogger(YoungConsumerNativeTest.class);

    private YoungClient client;

    @Before
    public void init() {
        client = new YoungClient("127.0.0.1:2181", "zookeeper", "1.0.0", "young", 3000L, "jdk", false, false);
    }

    @Test
    public void testAsyncInterfaceRpc() {
        IAsyncObjectProxy proxy = client.createAsync(DemoService.class);
        YoungFuture future = proxy.call("hello", "young");
        logger.info("返回的结果数据 ==> {}", future.get());
        client.shutdown();
    }

    @Test
    public void testInterfaceRpc() {
        DemoService demoService = client.create(DemoService.class);
        logger.info("返回的结果数据 ==> {}", demoService.hello("young"));
        client.shutdown();
    }
}
