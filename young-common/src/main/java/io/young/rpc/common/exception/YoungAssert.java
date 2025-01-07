package io.young.rpc.common.exception;

import org.springframework.util.ObjectUtils;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.Map;
import java.util.Objects;

/**
 * @author YoungCR
 * @date 2024/12/16 17:11
 * @descritpion YoungAssert 断言工具类
 */

public class YoungAssert {

    public static void throwAssertIfTrue(Boolean express, String message) {
        if (express) {
            throw new AssertException(message);
        }
    }

    public static void throwAssertIfFalse(Boolean express, String message) {
        if (!express) {
            throw new AssertException(message);
        }
    }

    public static <T extends Number> void throwAssertIfZeroOrNegative(T number, String message) {
        if (number.doubleValue() <= 0) {
            throw new AssertException(message);
        }
    }

    public static void throwAssertIfZeroOrNegative(BigDecimal number, String message) {
        if (number.compareTo(BigDecimal.ZERO) <= 0) {
            throw new AssertException(message);
        }
    }

    public static void throwAssertIfNullOrEmpty(Object obj, String message) {
        if (ObjectUtils.isEmpty(obj)) {
            throw new AssertException(message);
        }
    }

    public static void throwAssertIfBlank(String text, String message) {
        assert text != null;
        if (text.isBlank()) {
            throw new AssertException(message);
        }
    }

    public static void throwAssertIfEqual(Object s1, Object s2, String message) {
        if (Objects.equals(s1, s2)) {
            throw new AssertException(message);
        }
    }

    public static void throwRuntimeIfEqual(Object s1, Object s2, String message) {
        if (Objects.equals(s1, s2)) {
            throw new RuntimeException(message);
        }
    }

    public static void throwAssertIfNotEqual(String s1, String s2, String message) {
        if (!Objects.equals(s1, s2)) {
            throw new AssertException(message);
        }
    }

    public static <T extends Number & Comparable<T>> void throwAssertIfLessThan(T num1, T num2, String message) {
        if (num1.compareTo(num2) < 0) {
            throw new AssertException(message);
        }
    }
}
