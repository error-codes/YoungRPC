package io.young.rpc.common.util;

import java.util.concurrent.atomic.AtomicLong;

/**
 * @author YoungCR
 * @date 2024/12/23 17:03
 * @descritpion IDFactory ID工厂类
 */
public class IDFactory {

    private static final AtomicLong ID_GENERATOR = new AtomicLong(0);

    public static Long getId() {
        return ID_GENERATOR.incrementAndGet();
    }
}
