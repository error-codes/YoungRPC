package io.young.rpc.common.exception;

import java.io.Serial;

/**
 * @author YoungCR
 * @date 2024/12/16 14:07
 * @descritpion YoungException 系统异常
 */
public class YoungException extends RuntimeException {

    @Serial
    private static final long serialVersionUID = 8002097642129141935L;

    protected String exceptionMessage;

    public YoungException() {
        super();
    }

    public YoungException(String message) {
        super(message);
    }

    public YoungException(Throwable cause) {
        super(cause);
    }

    public YoungException(String message, Throwable cause) {
        super(message, cause);
    }

    protected YoungException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

    @Override
    public String getMessage() {
        return this.exceptionMessage != null ? this.exceptionMessage : super.getMessage();
    }

    public void appendMessage(String messageToAppend) {
        this.exceptionMessage = getMessage() + messageToAppend;
    }
}
