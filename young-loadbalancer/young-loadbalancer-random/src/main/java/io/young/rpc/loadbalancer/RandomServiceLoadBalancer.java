package io.young.rpc.loadbalancer;

import io.young.rpc.common.exception.YoungAssert;
import io.young.rpc.common.util.RespUtils;
import io.young.rpc.loadbalancer.api.ServiceLoadBalancer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Random;

/**
 * @author YoungCR
 * @date 2025/3/26 11:08
 * @descritpion RandomServiceLoadBalancer 随机负载均衡算法
 */
public class RandomServiceLoadBalancer<T> implements ServiceLoadBalancer<T> {

    private static final Logger logger = LoggerFactory.getLogger(RandomServiceLoadBalancer.class);

    @Override
    public T select(List<T> servers, int hashCode) {
        logger.info("基于随机算法的负载均衡策略");
        YoungAssert.throwAssertIfNullOrEmpty(servers, RespUtils.getContent("Null.2"));
        Random random = new Random();
        int idx = random.nextInt(servers.size());
        return servers.get(idx);
    }
}
