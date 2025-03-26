package io.young.rpc.codec;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import io.young.rpc.common.exception.ExceptionFactory;
import io.young.rpc.common.exception.WrongArgumentException;
import io.young.rpc.common.util.RespUtils;
import io.young.rpc.common.util.SerializationUtils;
import io.young.rpc.constants.YoungConstants;
import io.young.rpc.protocol.YoungProtocol;
import io.young.rpc.protocol.enumeration.RpcType;
import io.young.rpc.protocol.header.YoungHeader;
import io.young.rpc.protocol.request.YoungRequest;
import io.young.rpc.protocol.response.YoungResponse;
import io.young.rpc.serialization.api.Serialization;

import java.nio.charset.StandardCharsets;
import java.util.List;

/**
 * @author YoungCR
 * @date 2024/12/24 9:20
 * @descritpion YoungDecoder 解码器
 */
public class YoungDecoder extends ByteToMessageDecoder implements YoungCodec {

    @Override
    protected void decode(ChannelHandlerContext channelHandlerContext, ByteBuf byteBuf, List<Object> list) {
        if (byteBuf.readableBytes() < YoungConstants.HEADER_TOTAL_LEN) {
            return;
        }
        byteBuf.markReaderIndex();
        short magic = byteBuf.readShort();
        if (magic != YoungConstants.MAGIC) {
            throw ExceptionFactory.createException(WrongArgumentException.class, RespUtils.getContent("Protocol.0", magic));
        }

        byte packetType = byteBuf.readByte();
        byte status = byteBuf.readByte();
        long requestId = byteBuf.readLong();

        ByteBuf serializationByteBuf = byteBuf.readBytes(SerializationUtils.MAX_SERIALIZATION_TYPE_LENGTH);
        String serializationType = SerializationUtils.removePaddingCharacters(serializationByteBuf.toString(StandardCharsets.UTF_8));

        int dataLength = byteBuf.readInt();
        if (byteBuf.readableBytes() < dataLength) {
            byteBuf.resetReaderIndex();
            return;
        }

        byte[] data = new byte[dataLength];
        byteBuf.readBytes(data);
        RpcType rpcType = RpcType.findByType(packetType);
        if (rpcType == null) {
            return;
        }

        YoungHeader header = new YoungHeader();
        header.setMagic(magic);
        header.setStatus(status);
        header.setRequestId(requestId);
        header.setPacketType(packetType);
        header.setSerializationType(serializationType);
        header.setDataLength(dataLength);
        // TODO: Serialization 是扩展点
        Serialization serialization = getJdkSerialization();
        switch (rpcType) {
            case REQUEST -> {
                YoungRequest request = serialization.deserialize(data, YoungRequest.class);
                if (request != null) {
                    YoungProtocol<YoungRequest> protocol = new YoungProtocol<>();
                    protocol.setHeader(header);
                    protocol.setBody(request);
                    list.add(protocol);
                }
            }

            case RESPONSE -> {
                YoungResponse response = serialization.deserialize(data, YoungResponse.class);
                if (response != null) {
                    YoungProtocol<YoungResponse> protocol = new YoungProtocol<>();
                    protocol.setHeader(header);
                    protocol.setBody(response);
                    list.add(protocol);
                }
            }

            case HEARTBEAT -> {
                // TODO: 心跳处理
            }

            default -> {

            }
        }
    }
}
