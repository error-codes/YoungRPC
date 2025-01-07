package io.young.rpc.test.provider.single;

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
        YoungSingleServer youngSingleServer = new YoungSingleServer("127.0.0.1:27880", "io.young.rpc.test", "cglib");
        youngSingleServer.startNettyServer();
    }
}
