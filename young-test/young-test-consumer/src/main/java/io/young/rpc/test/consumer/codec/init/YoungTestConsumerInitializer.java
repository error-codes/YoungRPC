package io.young.rpc.test.consumer.codec.init;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.young.rpc.codec.YoungDecoder;
import io.young.rpc.codec.YoungEncoder;
import io.young.rpc.test.consumer.codec.handler.YoungConsumerHandler;

/**
 * @author YoungCR
 * @date 2024/12/24 15:16
 * @descritpion YoungTestConsumerInitializer
 */
public class YoungTestConsumerInitializer extends ChannelInitializer<SocketChannel> {

    @Override
    protected void initChannel(SocketChannel socketChannel) throws Exception {
        ChannelPipeline cp = socketChannel.pipeline();
        cp.addLast(new YoungEncoder());
        cp.addLast(new YoungDecoder());
        cp.addLast(new YoungConsumerHandler());
    }
}
