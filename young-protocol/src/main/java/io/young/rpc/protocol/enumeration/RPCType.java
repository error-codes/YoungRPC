package io.young.rpc.protocol.enumeration;

/**
 * @author YoungCR
 * @date 2024/12/23 13:28
 * @descritpion RPCType 通信消息类型
 */
public enum RPCType {

    /**
     * 请求消息
     */
    REQUEST((byte) 1),
    /**
     * 响应消息
     */
    RESPONSE((byte) 2),
    /**
     * 心跳消息
     */
    HEARTBEAT((byte) 3);

    final byte type;

    RPCType(byte type) {
        this.type = type;
    }

    public byte getType() {
        return type;
    }

    public static RPCType findByType(byte type) {
        for (RPCType value : RPCType.values()) {
            if (value.type == type) {
                return value;
            }
        }
        return null;
    }
}
