package io.young.rpc.protocol.enumeration;

/**
 * @author YoungCR
 * @date 2024/12/23 13:28
 * @descritpion RpcType 通信消息类型
 */
public enum RpcType {

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

    RpcType(byte type) {
        this.type = type;
    }

    public byte getType() {
        return type;
    }

    public static RpcType findByType(byte type) {
        for (RpcType value : RpcType.values()) {
            if (value.type == type) {
                return value;
            }
        }
        return null;
    }
}
