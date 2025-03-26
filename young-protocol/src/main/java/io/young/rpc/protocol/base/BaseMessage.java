package io.young.rpc.protocol.base;

import java.io.Serial;
import java.io.Serializable;

/**
 * @author YoungCR
 * @date 2024/12/23 13:34
 * @descritpion BaseMessage 基础消息类
 */
public class BaseMessage implements Serializable {

    @Serial
    private static final long serialVersionUID = 2404568700825381178L;

    /**
     * 单向发送
     */
    private boolean oneway;

    /**
     * 异步发送
     */
    private boolean async;

    public boolean isOneway() {
        return oneway;
    }

    public BaseMessage oneway(boolean oneway) {
        this.oneway = oneway;
        return this;
    }

    public boolean isAsync() {
        return async;
    }

    public BaseMessage async(boolean async) {
        this.async = async;
        return this;
    }
}
