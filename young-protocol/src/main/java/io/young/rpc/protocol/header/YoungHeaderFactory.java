package io.young.rpc.protocol.header;

import io.young.rpc.common.util.IDFactory;
import io.young.rpc.constants.YoungConstants;
import io.young.rpc.protocol.enumeration.RpcType;

/**
 * @author YoungCR
 * @date 2024/12/23 15:24
 * @descritpion YoungHeaderFactory 请求头工厂类
 */
public class YoungHeaderFactory {

    public static YoungHeader createYoungHeader(String serializationType) {
        YoungHeader header = new YoungHeader();

        Long requestId = IDFactory.getId();
        header.setMagic(YoungConstants.MAGIC);
        header.setRequestId(requestId);
        header.setPacketType(RpcType.REQUEST.getType());
        header.setStatus((byte) 0x01);
        header.setSerializationType(serializationType);
        return header;
    }
}
