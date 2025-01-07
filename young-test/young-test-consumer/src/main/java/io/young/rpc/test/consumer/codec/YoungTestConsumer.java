package io.young.rpc.test.consumer.codec;


import io.young.rpc.consumer.common.YoungConsumer;
import io.young.rpc.protocol.YoungProtocol;
import io.young.rpc.protocol.header.YoungHeaderFactory;
import io.young.rpc.protocol.request.YoungRequest;

/**
 * @author YoungCR
 * @date 2024/12/24 15:17
 * @descritpion YoungTestConsumer
 */
public class YoungTestConsumer {

    public static void main(String[] args) throws InterruptedException {
        YoungConsumer consumer = YoungConsumer.INSTANCE();
        consumer.sendRequest(getYoungRequestProtocol());
        Thread.sleep(2000);
        consumer.close();
    }

    private static YoungProtocol<YoungRequest> getYoungRequestProtocol() {
        YoungProtocol<YoungRequest> requestYoungProtocol = new YoungProtocol<>();
        requestYoungProtocol.setHeader(YoungHeaderFactory.createYoungHeader("jdk"));
        YoungRequest request = new YoungRequest()
                .className("io.young.rpc.test.api.DemoService")
                .group("young")
                .methodName("hello")
                .parameters(new Object[]{"young"})
                .parameterTypes(new Class[]{String.class})
                .version("1.0.0")
                .async(false)
                .oneway(false);

        requestYoungProtocol.setBody(request);
        return requestYoungProtocol;
    }
}
