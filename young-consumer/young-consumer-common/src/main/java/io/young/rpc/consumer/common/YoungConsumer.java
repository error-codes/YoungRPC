package io.young.rpc.consumer.common;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoop;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.young.rpc.annotation.YoungReference;
import io.young.rpc.consumer.common.handler.YoungConsumerHandler;
import io.young.rpc.consumer.common.initializer.YoungConsumerInitializer;
import io.young.rpc.protocol.YoungProtocol;
import io.young.rpc.protocol.request.YoungRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author YoungCR
 * @date 2024/12/27 16:28
 * @descritpion YoungConsumer
 */
public class YoungConsumer {

    private final Logger logger = LoggerFactory.getLogger(YoungConsumer.class);

    private final Bootstrap bootstrap;
    private final EventLoopGroup eventLoopGroup;

    private static volatile YoungConsumer INSTANCE;

    private static Map<String, YoungConsumerHandler> handlerMap = new ConcurrentHashMap<>();

    public YoungConsumer() {
        this.bootstrap = new Bootstrap();
        this.eventLoopGroup = new NioEventLoopGroup(4);
        bootstrap.group(eventLoopGroup).channel(NioSocketChannel.class)
                .handler(new YoungConsumerInitializer());
    }

    public static YoungConsumer INSTANCE() {
        if (INSTANCE == null) {
            synchronized (YoungConsumer.class) {
                if (INSTANCE == null) {
                    INSTANCE = new YoungConsumer();
                }
            }
        }
        return INSTANCE;
    }

    public void close() {
        eventLoopGroup.shutdownGracefully();
    }

    public void sendRequest(YoungProtocol<YoungRequest> requestYoungProtocol) throws InterruptedException {
        // TODO: 等待注册中心实现
        String serviceAddress = "127.0.0.1";
        int port = 27880;
        String key = serviceAddress.concat("_").concat(String.valueOf(port));
        YoungConsumerHandler handler = handlerMap.get(key);
        if (handler == null) {
            handler = getYoungConsumerHandler(serviceAddress, port);
            handlerMap.put(key, handler);
        } else if (!handler.getChannel().isActive()) {
            handler.close();
            handler = getYoungConsumerHandler(serviceAddress, port);
            handlerMap.put(key, handler);
        }
        handler.sendRequest(requestYoungProtocol);
    }

    private YoungConsumerHandler getYoungConsumerHandler(String serviceAddress, int port) throws InterruptedException {
        ChannelFuture channelFuture = bootstrap.connect(serviceAddress, port).sync();
        channelFuture.addListener((ChannelFutureListener) listener -> {
            if (channelFuture.isSuccess()) {
                logger.info("connect server {} on port {} success.", serviceAddress, port);
            } else {
                logger.error("connect server {} on port {} failed.", serviceAddress, port);
                channelFuture.cause().printStackTrace();
                eventLoopGroup.shutdownGracefully();
            }
        });

        return channelFuture.channel().pipeline().get(YoungConsumerHandler.class);
    }
}
