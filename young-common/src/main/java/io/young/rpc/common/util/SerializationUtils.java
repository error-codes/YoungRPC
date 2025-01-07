package io.young.rpc.common.util;

import java.util.stream.IntStream;

/**
 * @author YoungCR
 * @date 2024/12/23 19:14
 * @descritpion SerializationUtils 序列化工具类
 */
public class SerializationUtils {

    /**
     * 填充使用的字符
     */
    private static final String FILL_CHAR = "0";

    /**
     * 最大序列化类型长度
     */
    public static final int MAX_TYPE_LENGTH = 16;

    /**
     * 为长度不足16的字符串填充指定字符
     * @param input 原始字符串
     * @return 填充后的字符串
     */
    public static String padToFixedLength(String input) {
        input = ensureNonNull(input);
        int fillCount = MAX_TYPE_LENGTH - input.length();
        if (fillCount <= 0) {
            return input;
        }
        return input + FILL_CHAR.repeat(fillCount);
    }

    /**
     * 去除字符串中的填充字符
     * @param input 原始字符串
     * @return 去除填充字符后的字符串
     */
    public static String removeFillCharacters(String input) {
        return ensureNonNull(input).replace(FILL_CHAR, "");
    }

    /**
     * 确保字符串非 null
     * @param input 原始字符串
     * @return 非 null 字符串
     */
    public static String ensureNonNull(String input) {
        return input == null ? "" : input;
    }
}
