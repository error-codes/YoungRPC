package io.young.rpc.common.exception;

import java.io.Serial;

/**
 * @author YoungCR
 * @date 2024/12/16 19:51
 * @descritpion AssertException 断言异常类
 */
public class AssertException extends RuntimeException {

    @Serial
    private static final long serialVersionUID = 4378375535928417702L;

    public AssertException(String message) {
        super(message);
    }

}
