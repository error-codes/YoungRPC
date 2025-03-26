package io.young.rpc.proxy.api.consumer;

import io.young.rpc.protocol.YoungProtocol;
import io.young.rpc.protocol.request.YoungRequest;
import io.young.rpc.proxy.api.future.YoungFuture;
import io.young.rpc.registry.api.RegistryService;

/**
 * @author YoungCR
 * @date 2025/2/7 17:19
 * @descritpion Consumer 消费者
 */
public interface Consumer {

    /**
     * 发送请求
     *
     * @param requestYoungProtocol 请求协议
     * @param registryService      注册中心服务
     * @return YoungFuture
     * @throws InterruptedException 中断异常
     */
    YoungFuture sendRequest(YoungProtocol<YoungRequest> requestYoungProtocol, RegistryService registryService) throws Exception;
}
