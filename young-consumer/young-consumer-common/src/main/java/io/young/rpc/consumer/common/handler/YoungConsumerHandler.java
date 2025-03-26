package io.young.rpc.consumer.common.handler;

import com.alibaba.fastjson2.JSON;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.young.rpc.consumer.common.context.YoungContext;
import io.young.rpc.protocol.YoungProtocol;
import io.young.rpc.protocol.header.YoungHeader;
import io.young.rpc.protocol.request.YoungRequest;
import io.young.rpc.protocol.response.YoungResponse;
import io.young.rpc.proxy.api.future.YoungFuture;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.SocketAddress;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author YoungCR
 * @date 2024/12/27 14:25
 * @descritpion YoungConsumerHandler 消费者处理器
 */
public class YoungConsumerHandler extends SimpleChannelInboundHandler<YoungProtocol<YoungResponse>> {

    private static final Logger logger = LoggerFactory.getLogger(YoungConsumerHandler.class);

    private volatile Channel channel;
    private SocketAddress remoteAddress;
    private final Map<Long, YoungFuture> pendingRpc = new ConcurrentHashMap<>();


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
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, YoungProtocol<YoungResponse> youngResponseYoungProtocol) {
        if (youngResponseYoungProtocol == null) {
            return;
        }
        logger.info("服务消费者接收到的数据 ==> {}", JSON.toJSONString(youngResponseYoungProtocol));
        YoungHeader header = youngResponseYoungProtocol.getHeader();
        long requestId = header.getRequestId();
        YoungFuture future = pendingRpc.remove(requestId);
        if (future != null) {
            future.done(youngResponseYoungProtocol);
        }
    }

    public YoungFuture sendRequest(YoungProtocol<YoungRequest> requestYoungProtocol, boolean async, boolean oneway) {
        logger.info("服务消费者发送的数据 ==> {}", JSON.toJSONString(requestYoungProtocol));
        return oneway ? this.onewaySendRequest(requestYoungProtocol) : async ? this.asyncSendRequest(requestYoungProtocol) : this.syncSendRequest(requestYoungProtocol);
    }

    public YoungFuture syncSendRequest(YoungProtocol<YoungRequest> requestYoungProtocol) {
        YoungFuture future = new YoungFuture(requestYoungProtocol);
        YoungHeader header = requestYoungProtocol.getHeader();
        long requestId = header.getRequestId();
        pendingRpc.put(requestId, future);
        channel.writeAndFlush(requestYoungProtocol);
        return future;
    }

    public YoungFuture asyncSendRequest(YoungProtocol<YoungRequest> requestYoungProtocol) {
        YoungFuture future = new YoungFuture(requestYoungProtocol);
        YoungHeader header = requestYoungProtocol.getHeader();
        long requestId = header.getRequestId();
        pendingRpc.put(requestId, future);
        YoungContext.getContext().setYoungFuture(future);
        channel.writeAndFlush(requestYoungProtocol);
        return null;
    }

    public YoungFuture onewaySendRequest(YoungProtocol<YoungRequest> requestYoungProtocol) {
        channel.writeAndFlush(requestYoungProtocol);
        return null;
    }

    public void close() {
        channel.writeAndFlush(Unpooled.EMPTY_BUFFER).addListener(ChannelFutureListener.CLOSE);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        logger.error("client caught exception", cause);
    }

    public Map<Long, YoungFuture> getPendingRpc() {
        return pendingRpc;
    }
}
