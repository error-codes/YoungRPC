package io.young.rpc.common.exception;

/**
 * @author YoungCR
 * @date 2024/12/16 14:07
 * @descritpion ExceptionFactory 异常工厂类
 */
public class ExceptionFactory {

    public static YoungException createException(String message) {
        return createException(YoungException.class, message);
    }

    public static <T extends YoungException> T createException(Class<T> clazz, String message) {
        T exception;
        try {
            exception = clazz.getConstructor(String.class).newInstance(message);
        } catch (Throwable e) {
            exception = (T) new YoungException(message);
        }
        return exception;
    }

    public static YoungException createException(String message, Throwable cause) {
        return createException(YoungException.class, message, cause);
    }

    public static <T extends YoungException> T createException(Class<T> clazz, String message, Throwable cause) {
        T exception = createException(clazz, message);
        if (cause != null) {
            try {
                exception.initCause(cause);
            } catch (Throwable t) {
                // we're not going to muck with that here, since it's an error condition anyway!
            }
        }
        return exception;
    }
}
