package io.young.rpc.proxy.api.callback;

/**
 * @author YoungCR
 * @date 2025/2/7 17:17
 * @descritpion AsyncRpcCallback 异步回调接口
 */
public interface AsyncRpcCallback {

    /**
     * 成功的回调方法
     *
     * @param result 结果
     */
    void onSuccess(Object result);

    /**
     * 异常的回调方法
     *
     * @param e 异常
     */
    void onException(Exception e);
}
