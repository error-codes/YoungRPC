package io.young.rpc.common.util;

import io.young.rpc.common.exception.ExceptionFactory;
import io.young.rpc.common.exception.WrongArgumentException;

import java.text.MessageFormat;
import java.util.ResourceBundle;

/**
 * @author YoungCR
 * @date 2024/12/16 13:53
 * @descritpion ErrorMessageUtils 错误异常信息处理工具
 */
public class RespUtils {

    private static final ResourceBundle MESSAGE_RESOURCES;

    static {
        MESSAGE_RESOURCES = ResourceBundle.getBundle("ErrorMessages");
    }

    public static String getContent(String key, Object... args) {
        if (key == null) {
            throw ExceptionFactory.createException(WrongArgumentException.class, "key cannot be null.");
        }

        String message = MESSAGE_RESOURCES.getString(key);

        return MessageFormat.format(message, args);
    }

}
