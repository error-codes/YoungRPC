package io.young.rpc.common.exception;

import java.io.Serial;

/**
 * @author YoungCR
 * @date 2024/12/23 19:10
 * @descritpion SerializerException 序列化异常类
 */
public class SerializerException extends YoungException {

    @Serial
    private static final long serialVersionUID = -2099599518804532110L;

    public SerializerException(String message, Throwable cause) {
        super(message, cause);
    }

    public SerializerException(String message) {
        super(message);
    }

    public SerializerException() {
    }
}
