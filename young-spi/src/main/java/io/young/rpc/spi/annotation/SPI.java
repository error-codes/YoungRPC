package io.young.rpc.spi.annotation;

import java.lang.annotation.*;

/**
 * @author YoungCR
 * @date 2025/3/26 13:27
 * @descritpion SPI
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
public @interface SPI {

    String value() default "";
}
