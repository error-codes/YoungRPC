package io.young.rpc.proxy.api.async;

import io.young.rpc.proxy.api.future.YoungFuture;

/**
 * @author YoungCR
 * @date 2025/3/4 16:35
 * @descritpion IAsyncObjectProxy 异步对象代理
 */
public interface IAsyncObjectProxy {

    /**
     * 异步代理对象
     * @param funcName 方法名
     * @param args 参数
     * @return 异步Future
     */
    YoungFuture call(String funcName, Object... args);
}
