package io.young.rpc.loadbalancer.api;

import java.util.List;

/**
 * @author YoungCR
 * @date 2025/3/25 18:58
 * @descritpion ServiceLoadBalancer 负载均衡接口
 */
public interface ServiceLoadBalancer<T> {

    /**
     * 负载均衡选取节点
     *
     * @param servers  服务列表
     * @param hashCode hashCode
     * @return T
     */
    T select(List<T> servers, int hashCode);
}
