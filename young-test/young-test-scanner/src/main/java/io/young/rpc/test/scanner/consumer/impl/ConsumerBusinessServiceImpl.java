package io.young.rpc.test.scanner.consumer.impl;

import io.young.rpc.annotation.YoungReference;
import io.young.rpc.test.scanner.consumer.ConsumerBusinessService;
import io.young.rpc.test.scanner.service.DemoService;

/**
 * @author YoungCR
 * @date 2024/12/17 14:10
 * @descritpion ConsumerBusinessServiceImpl
 */
public class ConsumerBusinessServiceImpl implements ConsumerBusinessService {

    @YoungReference(registryType = "zookeeper", registryAddress = "127.0.0.1:2181", version = "1.0.0", group = "young")
    private DemoService demoService;
}
