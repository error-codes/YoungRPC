package io.young.test.provider.single;

import io.young.rpc.provider.YoungSingleServer;
import org.junit.Test;

/**
 * @author YoungCR
 * @date 2024/12/23 11:06
 * @descritpion YoungSingleServerTest
 */
public class YoungSingleServerTest {

    @Test
    public void startYoungSingleServer() {
        YoungSingleServer youngSingleServer = new YoungSingleServer("127.0.0.1:27880", "127.0.0.1:2181", "zookeeper", "io.young.test", "cglib");
        youngSingleServer.startNettyServer();
    }
}
