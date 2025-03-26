package io.young.test.consumer.codec;


import io.netty.bootstrap.Bootstrap;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.young.test.consumer.codec.init.YoungTestConsumerInitializer;

/**
 * @author YoungCR
 * @date 2024/12/24 15:17
 * @descritpion YoungTestConsumer
 */
public class YoungTestConsumer {

    public static void main(String[] args) throws InterruptedException {
        Bootstrap      bootstrap      = new Bootstrap();
        EventLoopGroup eventLoopGroup = new NioEventLoopGroup(4);
        try {
            bootstrap.group(eventLoopGroup)
                    .channel(NioSocketChannel.class)
                    .handler(new YoungTestConsumerInitializer());
            bootstrap.connect("127.0.0.1", 27880).sync();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            Thread.sleep(2000);
            eventLoopGroup.shutdownGracefully();
        }

    }
}
