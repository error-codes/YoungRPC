package io.young.rpc.protocol.enumeration;

/**
 * @author YoungCR
 * @date 2024/12/24 17:03
 * @descritpion RPCStatus
 */
public enum RPCStatus {

    SUCCESS((byte) 1),

    FAILURE((byte) 0);

    private final byte code;

    RPCStatus(byte code) {
        this.code = code;
    }

    public byte getCode() {
        return code;
    }
}
