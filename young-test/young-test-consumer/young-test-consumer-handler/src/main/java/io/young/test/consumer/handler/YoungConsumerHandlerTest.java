package io.young.test.consumer.handler;

import io.young.rpc.consumer.common.YoungConsumer;
import io.young.rpc.consumer.common.callback.AsyncRPCCallback;
import io.young.rpc.consumer.common.context.YoungContext;
import io.young.rpc.consumer.common.future.YoungFuture;
import io.young.rpc.protocol.YoungProtocol;
import io.young.rpc.protocol.header.YoungHeaderFactory;
import io.young.rpc.protocol.request.YoungRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ExecutionException;

/**
 * @author YoungCR
 * @date 2024/12/24 14:06
 * @descritpion YoungConsumerHandler
 */
public class YoungConsumerHandlerTest {

    private static final Logger logger = LoggerFactory.getLogger(YoungConsumerHandlerTest.class);

    public static void main(String[] args) throws InterruptedException, ExecutionException {
        YoungConsumer consumer = new YoungConsumer();
        YoungFuture   future   = consumer.sendRequest(getYoungRequestProtocol());
        future.addCallback(new AsyncRPCCallback() {
            @Override
            public void onSuccess(Object result) {
                LOGGER.info("从服务消费者获取到的数据 ==> {}", result);
            }

            @Override
            public void onException(Exception e) {
                LOGGER.info("发生了异常 ==> {}", e.getMessage());
            }
        });
        Thread.sleep(2000);
        consumer.close();
    }

    public static void mainSync(String[] args) throws InterruptedException, ExecutionException {
        YoungConsumer consumer = new YoungConsumer();
        YoungFuture   future   = consumer.sendRequest(getYoungRequestProtocol());
        LOGGER.info("从服务消费者获取到的数据 ==> {}", future.get());
        consumer.close();
    }

    public static void mainAsync(String[] args) throws InterruptedException, ExecutionException {
        YoungConsumer consumer = new YoungConsumer();
        consumer.sendRequest(getYoungRequestProtocolAsync());
        YoungFuture future = YoungContext.getContext().getYoungFuture();
        LOGGER.info("从服务消费者获取到的数据 ==> {}", future.get());
        consumer.close();
    }

    public static void mainOneway(String[] args) throws InterruptedException, ExecutionException {
        YoungConsumer consumer = new YoungConsumer();
        consumer.sendRequest(getYoungRequestProtocolOneway());
        consumer.close();
    }

    private static YoungProtocol<YoungRequest> getYoungRequestProtocol() {
        // 模拟发送数据
        YoungProtocol<YoungRequest> protocol = new YoungProtocol<>();
        protocol.setHeader(YoungHeaderFactory.createYoungHeader("jdk"));

        YoungRequest request = new YoungRequest();
        request.className("io.young.rpc.test.api.DemoService")
                .group("young")
                .methodName("hello")
                .parameters(new Object[]{"young"})
                .parameterTypes(new Class[]{String.class})
                .version("1.0.0")
                .async(false)
                .oneway(false);

        protocol.setBody(request);
        return protocol;
    }

    private static YoungProtocol<YoungRequest> getYoungRequestProtocolAsync() {
        // 模拟发送数据
        YoungProtocol<YoungRequest> protocol = new YoungProtocol<>();
        protocol.setHeader(YoungHeaderFactory.createYoungHeader("jdk"));

        YoungRequest request = new YoungRequest();
        request.className("io.young.rpc.test.api.DemoService")
                .group("young")
                .methodName("hello")
                .parameters(new Object[]{"young"})
                .parameterTypes(new Class[]{String.class})
                .version("1.0.0")
                .async(true)
                .oneway(false);

        protocol.setBody(request);
        return protocol;
    }

    private static YoungProtocol<YoungRequest> getYoungRequestProtocolOneway() {
        // 模拟发送数据
        YoungProtocol<YoungRequest> protocol = new YoungProtocol<>();
        protocol.setHeader(YoungHeaderFactory.createYoungHeader("jdk"));

        YoungRequest request = new YoungRequest();
        request.className("io.young.rpc.test.api.DemoService")
                .group("young")
                .methodName("hello")
                .parameters(new Object[]{"young"})
                .parameterTypes(new Class[]{String.class})
                .version("1.0.0")
                .async(false)
                .oneway(true);

        protocol.setBody(request);
        return protocol;
    }
}
