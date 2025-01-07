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
import io.young.rpc.protocol.enumeration.RPCType;
import io.young.rpc.protocol.header.YoungHeader;
import io.young.rpc.protocol.request.YoungRequest;
import io.young.rpc.protocol.response.YoungResponse;
import io.young.rpc.serialization.api.Serialization;
import io.young.rpc.serialization.jdk.JdkSerialization;

import java.util.List;

/**
 * @author YoungCR
 * @date 2024/12/24 9:20
 * @descritpion YoungDecoder
 */
public class YoungDecoder extends ByteToMessageDecoder implements YoungCodec {

    @Override
    protected void decode(ChannelHandlerContext channelHandlerContext, ByteBuf byteBuf, List<Object> list) throws Exception {
        if (byteBuf.readableBytes() < YoungConstants.HEADER_TOTAL_LEN) {
            return;
        }
        byteBuf.markReaderIndex();
        short magic = byteBuf.readShort();
        if (magic != YoungConstants.MAGIC) {
            throw ExceptionFactory.createException(WrongArgumentException.class, RespUtils.getContent("Protocol.0", magic));
        }

        byte msgType = byteBuf.readByte();
        byte status     = byteBuf.readByte();
        long msgId = byteBuf.readLong();

        ByteBuf serializationByteBuf = byteBuf.readBytes(SerializationUtils.MAX_TYPE_LENGTH);
        String serializationType = SerializationUtils.removeFillCharacters(serializationByteBuf.toString());

        int dataLength = byteBuf.readInt();
        if (byteBuf.readableBytes() < dataLength) {
            byteBuf.resetReaderIndex();
            return;
        }

        byte[] data = new byte[dataLength];
        byteBuf.readBytes(data);
        RPCType rpcType = RPCType.findByType(msgType);
        if (rpcType == null) {
            return;
        }

        YoungHeader header = new YoungHeader();
        header.setMagic(magic);
        header.setStatus(status);
        header.setMsgId(msgId);
        header.setMsgType(msgType);
        header.setSerializationType(serializationType);
        header.setMsgLength(dataLength);
        // TODO: Serialization 是扩展点
        Serialization serialization = new JdkSerialization();
        switch (rpcType) {
            case REQUEST -> {
                YoungRequest request = serialization.deserialize(data, YoungRequest.class);
                if (request != null) {
                    YoungProtocol<YoungRequest> protocol = new YoungProtocol<>();
                    protocol.setHeader(header);
                    protocol.setBody(request);
                    list.add(protocol);
                }
                break;
            }
            case RESPONSE -> {
                YoungResponse response = serialization.deserialize(data, YoungResponse.class);
                if (response != null) {
                    YoungProtocol<YoungResponse> protocol = new YoungProtocol<>();
                    protocol.setHeader(header);
                    protocol.setBody(response);
                    list.add(protocol);
                }
                break;
            }
            case HEARTBEAT -> {
                // TODO: 心跳处理
                break;
            }
        }
    }
}
