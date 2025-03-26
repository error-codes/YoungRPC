package io.young.rpc.protocol.enumeration;

/**
 * @author YoungCR
 * @date 2024/12/24 17:03
 * @descritpion RpcStatus RPC状态
 */
public enum RpcStatus {

    /**
     * 成功
     */
    SUCCESS((byte) 1),

    /**
     * 失败
     */
    FAILURE((byte) 0);

    private final byte code;

    RpcStatus(byte code) {
        this.code = code;
    }

    public byte getCode() {
        return code;
    }
}
