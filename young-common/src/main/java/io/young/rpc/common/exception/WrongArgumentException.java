package io.young.rpc.common.exception;

import java.io.Serial;

/**
 * @author YoungCR
 * @date 2024/12/17 10:24
 * @descritpion WrongArgumentException 错误参数
 */

public class WrongArgumentException extends YoungException {

    @Serial
    private static final long serialVersionUID = 3991597077197801820L;

    public WrongArgumentException() {
        super();
    }

    public WrongArgumentException(String message) {
        super(message);
    }

    public WrongArgumentException(String message, Throwable cause) {
        super(message, cause);
    }

    public WrongArgumentException(Throwable cause) {
        super(cause);
    }

    public WrongArgumentException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
