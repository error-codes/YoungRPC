package io.young.rpc.consumer.common.context;

import io.young.rpc.proxy.api.future.YoungFuture;

/**
 * @author YoungCR
 * @date 2025/2/6 18:42
 * @descritpion YoungContext  上下文
 */
public class YoungContext {

    public YoungContext() {
    }

    /**
     * RPC Context 实例
     */
    private static final YoungContext AGENT = new YoungContext();

    /**
     * 存放 RPC Future 的InheritableThreadLocal
     */
    private static final InheritableThreadLocal<YoungFuture> RPC_FUTURE_INHERITABLE_THREAD_LOCAL = new InheritableThreadLocal<>();

    /**
     * 获取上下文
     */
    public static YoungContext getContext() {
        return AGENT;
    }

    /**
     * 将 RPC Future 保存到线程上下文
     */
    public void setYoungFuture(YoungFuture future) {
        RPC_FUTURE_INHERITABLE_THREAD_LOCAL.set(future);
    }

    /**
     * 获取 RPC Future
     */
    public YoungFuture getYoungFuture() {
        return RPC_FUTURE_INHERITABLE_THREAD_LOCAL.get();
    }

    /**
     * 移除 RPC Future
     */
    public void removeYoungFuture() {
        RPC_FUTURE_INHERITABLE_THREAD_LOCAL.remove();
    }
}
