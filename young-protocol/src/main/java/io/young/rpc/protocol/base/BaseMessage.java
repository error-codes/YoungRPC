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

    // 单向发送
    private boolean oneway;

    // 异步发送
    private boolean async;

    public boolean isOneway() {
        return oneway;
    }

    public void setOneway(boolean oneway) {
        this.oneway = oneway;
    }

    public boolean isAsync() {
        return async;
    }

    public void setAsync(boolean async) {
        this.async = async;
    }
}
