package io.young.rpc.consumer.common;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.young.rpc.common.help.ServiceHelper;
import io.young.rpc.common.threadpool.ClientThreadPool;
import io.young.rpc.consumer.common.handler.YoungConsumerHandler;
import io.young.rpc.consumer.common.helper.YoungConsumerHandlerHelper;
import io.young.rpc.consumer.common.initializer.YoungConsumerInitializer;
import io.young.rpc.protocol.YoungProtocol;
import io.young.rpc.protocol.meta.ServiceMeta;
import io.young.rpc.protocol.request.YoungRequest;
import io.young.rpc.proxy.api.consumer.Consumer;
import io.young.rpc.proxy.api.future.YoungFuture;
import io.young.rpc.registry.api.RegistryService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author YoungCR
 * @date 2024/12/27 16:28
 * @descritpion YoungConsumer
 */
public class YoungConsumer implements Consumer {

    private static final Logger logger = LoggerFactory.getLogger(YoungConsumer.class);
    private final Bootstrap bootstrap;
    private final EventLoopGroup eventLoopGroup;

    private static volatile YoungConsumer INSTANCE;

    private static final Map<String, YoungConsumerHandler> CONSUMER_HANDLER_MAP = new ConcurrentHashMap<>();

    public YoungConsumer() {
        this.bootstrap = new Bootstrap();
        this.eventLoopGroup = new NioEventLoopGroup(4);
        bootstrap.group(eventLoopGroup).channel(NioSocketChannel.class).handler(new YoungConsumerInitializer());
    }

    public static YoungConsumer getInstance() {
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
        YoungConsumerHandlerHelper.closeYoungConsumerHandler();
        eventLoopGroup.shutdownGracefully();
        ClientThreadPool.shutdown();
    }

    @Override
    public YoungFuture sendRequest(YoungProtocol<YoungRequest> requestYoungProtocol, RegistryService registryService) throws Exception {
        YoungRequest youngRequest = requestYoungProtocol.getBody();
        String serviceKey = ServiceHelper.buildServiceKey(youngRequest.getClassName(), youngRequest.getVersion(), youngRequest.getGroup());
        Object[] parameters = youngRequest.getParameters();
        int invokerHashCode = parameters == null || parameters.length == 0 ? serviceKey.hashCode() : parameters[0].hashCode();
        ServiceMeta serviceMeta = registryService.discovery(serviceKey, invokerHashCode);
        if (serviceMeta != null) {
            YoungConsumerHandler consumerHandler = YoungConsumerHandlerHelper.get(serviceMeta);
            if (consumerHandler == null) {
                consumerHandler = getYoungConsumerHandler(serviceMeta.getServiceAddress(), serviceMeta.getServicePort());
                YoungConsumerHandlerHelper.put(serviceMeta, consumerHandler);
            } else if (!consumerHandler.getChannel().isActive()) {
                consumerHandler.close();
                consumerHandler = getYoungConsumerHandler(serviceMeta.getServiceAddress(), serviceMeta.getServicePort());
                YoungConsumerHandlerHelper.put(serviceMeta, consumerHandler);
            }
            return consumerHandler.sendRequest(requestYoungProtocol, youngRequest.isAsync(), youngRequest.isOneway());
        }
        return null;
    }

    private YoungConsumerHandler getYoungConsumerHandler(String serviceAddress, int port) throws InterruptedException {
        ChannelFuture channelFuture = bootstrap.connect(serviceAddress, port).sync();
        channelFuture.addListener((ChannelFutureListener) listener -> {
            if (channelFuture.isSuccess()) {
                logger.info("connect server {} on port {} success.", serviceAddress, port);
            } else {
                logger.error("connect server {} on port {} failed. cause: ", serviceAddress, port, channelFuture.cause());
                eventLoopGroup.shutdownGracefully();
            }
        });

        return channelFuture.channel().pipeline().get(YoungConsumerHandler.class);
    }
}
