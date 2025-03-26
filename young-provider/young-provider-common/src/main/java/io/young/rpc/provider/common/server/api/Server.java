package io.young.rpc.provider.common.server.api;

/**
 * @author YoungCR
 * @date 2024/12/19 14:47
 * @descritpion Server 服务接口
 */
public interface Server {

    /**
     * 启动Netty服务
     */
    void startNettyServer();
}
