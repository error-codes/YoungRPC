package io.young.rpc.consumer.common.handler;

import com.alibaba.fastjson2.JSON;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.young.rpc.protocol.YoungProtocol;
import io.young.rpc.protocol.request.YoungRequest;
import io.young.rpc.protocol.response.YoungResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.SocketAddress;

/**
 * @author YoungCR
 * @date 2024/12/27 14:25
 * @descritpion YoungConsumerHandler
 */
public class YoungConsumerHandler extends SimpleChannelInboundHandler<YoungProtocol<YoungResponse>> {

    private final Logger logger = LoggerFactory.getLogger(YoungConsumerHandler.class);

    private volatile Channel       channel;
    private          SocketAddress remoteAddress;

    public Channel getChannel() {
        return channel;
    }

    public SocketAddress getRemoteAddress() {
        return remoteAddress;
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        super.channelActive(ctx);
        this.remoteAddress = this.channel.remoteAddress();
    }

    @Override
    public void channelRegistered(ChannelHandlerContext ctx) throws Exception {
        super.channelRegistered(ctx);
        this.channel = ctx.channel();
    }

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, YoungProtocol<YoungResponse> youngResponseYoungProtocol) throws Exception {
        logger.info("服务消费者接收到的数据 ==> {}", JSON.toJSONString(youngResponseYoungProtocol));
    }

    public void sendRequest(YoungProtocol<YoungRequest> requestYoungProtocol) {
        logger.info("服务消费者发送的数据 ==> {}", JSON.toJSONString(requestYoungProtocol));
        channel.writeAndFlush(requestYoungProtocol);
    }

    public void close() {
        channel.writeAndFlush(Unpooled.EMPTY_BUFFER).addListener(ChannelFutureListener.CLOSE);
    }
}
