package io.young.rpc.protocol.header;

import java.io.Serial;
import java.io.Serializable;

/**
 * @author YoungCR
 * @date 2024/12/23 14:32
 * @descritpion YoungHeader 消息头类
 */
public class YoungHeader implements Serializable {

    @Serial
    private static final long serialVersionUID = -6488750012583220101L;

    /*
     * 消息协议结构：
     * <p>
     * ➕---------------➕-------------------➕---------------➕-----------------➕
     *   |   魔数 (2 Byte)   |   报文类型 (1 Byte)   |   状态 (1 Byte)   |   消息ID (8 Byte)   |
     * ➕---------------➕-------------------➕---------------➕-----------------➕
     *   |                序列化类型  (16 Byte)              |          数据长度  (4 Byte)                      |
     * ➕-------------------------------------➕----------------------------------- ➕
     */


    /**
     * 魔数
     */
    private short magic;

    /**
     * 报文类型
     */
    private byte packetType;

    /**
     * 报文状态
     */
    private byte status;

    /**
     * 请求ID
     */
    private long requestId;

    /**
     * 序列化类型
     */
    private String serializationType;

    /**
     * 数据长度
     */
    private int dataLength;

    public short getMagic() {
        return magic;
    }

    public void setMagic(short magic) {
        this.magic = magic;
    }

    public byte getPacketType() {
        return packetType;
    }

    public void setPacketType(byte packetType) {
        this.packetType = packetType;
    }

    public byte getStatus() {
        return status;
    }

    public void setStatus(byte status) {
        this.status = status;
    }

    public long getRequestId() {
        return requestId;
    }

    public void setRequestId(long requestId) {
        this.requestId = requestId;
    }

    public String getSerializationType() {
        return serializationType;
    }

    public void setSerializationType(String serializationType) {
        this.serializationType = serializationType;
    }

    public int getDataLength() {
        return dataLength;
    }

    public void setDataLength(int dataLength) {
        this.dataLength = dataLength;
    }
}
