package io.young.rpc.test.consumer.codec.handler;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.young.rpc.protocol.YoungProtocol;
import io.young.rpc.protocol.header.YoungHeaderFactory;
import io.young.rpc.protocol.request.YoungRequest;
import io.young.rpc.protocol.response.YoungResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.Semaphore;

/**
 * @author YoungCR
 * @date 2024/12/24 14:06
 * @descritpion YoungConsumerHandler
 */
public class YoungConsumerHandler extends SimpleChannelInboundHandler<YoungProtocol<YoungResponse>> {

    private final Logger logger = LoggerFactory.getLogger(YoungConsumerHandler.class);

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        logger.info("开始发送数据......");
        YoungProtocol<YoungRequest> protocol = new YoungProtocol<>();
        protocol.setHeader(YoungHeaderFactory.createYoungHeader("jdk"));
        YoungRequest request = new YoungRequest()
                .className("io.young.rpc.test.api.DemoService")
                .group("young")
                .methodName("hello")
                .parameters(new Object[]{"young"})
                .parameterTypes(new Class[]{String.class})
                .version("1.0.0")
                .async(false)
                .oneway(false);
        logger.info("服务消费者发送的数据===>>>{}", JSONObject.toJSONString(protocol));
        ctx.writeAndFlush(protocol);
        logger.info("发送数据完毕...");

    }

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, YoungProtocol<YoungResponse> youngResponseYoungProtocol) throws Exception {
        logger.info("服务消费者接收到的数据 --> {}", JSON.toJSONString(youngResponseYoungProtocol));
    }
}
