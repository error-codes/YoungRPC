package io.young.rpc.provider.common.server.base;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.util.internal.StringUtil;
import io.young.rpc.codec.YoungDecoder;
import io.young.rpc.codec.YoungEncoder;
import io.young.rpc.provider.common.handler.YoungProviderHandler;
import io.young.rpc.provider.common.server.api.Server;
import io.young.rpc.registry.api.RegistryService;
import io.young.rpc.registry.api.config.RegistryConfig;
import io.young.rpc.registry.zookeeper.ZookeeperRegistryService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;


/**
 * @author YoungCR
 * @date 2024/12/19 15:03
 * @descritpion BaseServer 服务端基类
 */
public class BaseServer implements Server {

    private static final Logger logger = LoggerFactory.getLogger(BaseServer.class);

    /**
     * 主机地址
     */
    protected String host = "127.0.0.1";

    /**
     * 端口号
     */
    protected int port = 1207;

    /**
     * 反射类型
     */
    protected String reflectType;

    /**
     * 注册中心服务
     */
    protected RegistryService registryService;

    /**
     * 实体类关系
     */
    protected Map<String, Object> handlerMap = new HashMap<>();

    public BaseServer(String serverAddress, String registryAddress, String registryType, String reflectType) {
        if (!StringUtil.isNullOrEmpty(serverAddress)) {
            String[] address = serverAddress.split(":");
            this.host = address[0];
            this.port = Integer.parseInt(address[1]);
        }
        this.reflectType = reflectType;
        this.registryService = getRegistryService(registryAddress, registryType);
    }

    @Override
    public void startNettyServer() {
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        EventLoopGroup workerGroup = new NioEventLoopGroup();

        try {
            ServerBootstrap bootstrap = new ServerBootstrap();
            bootstrap.group(bossGroup, workerGroup).channel(NioServerSocketChannel.class)
                     .childHandler(new ChannelInitializer<SocketChannel>() {
                         @Override
                         protected void initChannel(SocketChannel socketChannel) throws Exception {
                             socketChannel.pipeline()
                                          // Todo: 预留编解码，需要自己实现自定义协议
                                          .addLast(new YoungDecoder())
                                          .addLast(new YoungEncoder())
                                          .addLast(new YoungProviderHandler(handlerMap, reflectType));
                         }
                     })
                     .option(ChannelOption.SO_BACKLOG, 128)
                     .childOption(ChannelOption.SO_KEEPALIVE, true);

            ChannelFuture future = bootstrap.bind(host, port).sync();
            logger.info("Server started on {}:{}", host, port);
            future.channel().closeFuture().sync();
        } catch (Exception e) {
            logger.error("RPC Server start error", e);
        } finally {
            workerGroup.shutdownGracefully();
            bossGroup.shutdownGracefully();
        }
    }

    private RegistryService getRegistryService(String registryAddress, String registryType) {
        // Todo: 后续等待SPI接入
        RegistryService service = null;
        try {
            service = new ZookeeperRegistryService();
            service.init(new RegistryConfig(registryAddress, registryType));
        } catch (Exception e) {
            logger.error("Registry service init error", e);
        }
        return service;
    }
}
