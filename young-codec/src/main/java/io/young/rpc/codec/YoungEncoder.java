package io.young.rpc.codec;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import io.young.rpc.common.util.SerializationUtils;
import io.young.rpc.protocol.YoungProtocol;
import io.young.rpc.protocol.header.YoungHeader;
import io.young.rpc.serialization.api.Serialization;

import java.nio.charset.StandardCharsets;

/**
 * @author YoungCR
 * @date 2024/12/23 20:54
 * @descritpion YoungEncoder  编码器
 */
public class YoungEncoder extends MessageToByteEncoder<YoungProtocol<Object>> implements YoungCodec {

    @Override
    protected void encode(ChannelHandlerContext channelHandlerContext, YoungProtocol<Object> protocol, ByteBuf byteBuf) {
        YoungHeader header = protocol.getHeader();
        byteBuf.writeShort(header.getMagic());
        byteBuf.writeByte(header.getPacketType());
        byteBuf.writeByte(header.getStatus());
        byteBuf.writeLong(header.getRequestId());
        String serializationType = header.getSerializationType();
        // TODO: Serialization 是扩展点
        Serialization serialization = getJdkSerialization();
        byteBuf.writeBytes(SerializationUtils.paddingToFixedLength(serializationType).getBytes(StandardCharsets.UTF_8));
        byte[] data = serialization.serialize(protocol.getBody());
        byteBuf.writeInt(data.length);
        byteBuf.writeBytes(data);
    }
}
