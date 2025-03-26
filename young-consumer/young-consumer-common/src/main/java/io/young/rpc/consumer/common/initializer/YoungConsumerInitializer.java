package io.young.rpc.consumer.common.initializer;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.young.rpc.codec.YoungDecoder;
import io.young.rpc.codec.YoungEncoder;
import io.young.rpc.consumer.common.handler.YoungConsumerHandler;

/**
 * @author YoungCR
 * @date 2024/12/27 16:14
 * @descritpion YoungConsumerInitializer
 */
public class YoungConsumerInitializer extends ChannelInitializer<SocketChannel> {

    @Override
    protected void initChannel(SocketChannel socketChannel) {
        ChannelPipeline pipeline = socketChannel.pipeline();
        pipeline.addLast(new YoungEncoder());
        pipeline.addLast(new YoungDecoder());
        pipeline.addLast(new YoungConsumerHandler());
    }
}
