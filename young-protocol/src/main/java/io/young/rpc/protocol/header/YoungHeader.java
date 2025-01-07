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

    /**
     * 消息协议结构：
     * <p>
     * +----------------+------------------+----------------+-----------------+
     * | 魔数【2 Byte】  | 报文类型【1 Byte】 | 状态【1 Byte】  | 消息ID【8 Byte】 |
     * +----------------------------------------------------------------------+
     * |        序列化类型【16 Byte】        |         数据长度【4 Byte】         |
     * +-----------------------------------+----------------------------------+
     */


     /**
     * 魔数
     */
    private short magic;

    /**
     * 报文类型
     */
    private byte msgType;

    /**
     * 报文状态
     */
    private byte status;

    /**
     * 消息ID
     */
    private long msgId;

    /**
     * 序列化类型
     */
    private String serializationType;

    /**
     * 消息长度
     */
    private int msgLength;

    public short getMagic() {
        return magic;
    }

    public void setMagic(short magic) {
        this.magic = magic;
    }

    public byte getMsgType() {
        return msgType;
    }

    public void setMsgType(byte msgType) {
        this.msgType = msgType;
    }

    public byte getStatus() {
        return status;
    }

    public void setStatus(byte status) {
        this.status = status;
    }

    public long getMsgId() {
        return msgId;
    }

    public void setMsgId(long msgId) {
        this.msgId = msgId;
    }

    public String getSerializationType() {
        return serializationType;
    }

    public void setSerializationType(String serializationType) {
        this.serializationType = serializationType;
    }

    public int getMsgLength() {
        return msgLength;
    }

    public void setMsgLength(int msgLength) {
        this.msgLength = msgLength;
    }
}
