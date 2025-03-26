package io.young.rpc.consumer.common.helper;

import io.young.rpc.consumer.common.handler.YoungConsumerHandler;
import io.young.rpc.protocol.meta.ServiceMeta;

import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author YoungCR
 * @date 2025/3/25 17:06
 * @descritpion YoungConsumerHandlerHelper
 */
public class YoungConsumerHandlerHelper {

    private static Map<String, YoungConsumerHandler> consumerHandlerMap;

    static {
        consumerHandlerMap = new ConcurrentHashMap<>(30);
    }

    private static String getKey(ServiceMeta serviceMeta) {
        return serviceMeta.getServiceAddress().concat("_").concat(String.valueOf(serviceMeta.getServicePort()));
    }

    public static void put(ServiceMeta serviceMeta, YoungConsumerHandler consumerHandler) {
        consumerHandlerMap.put(getKey(serviceMeta), consumerHandler);
    }

    public static YoungConsumerHandler get(ServiceMeta serviceMeta) {
        return consumerHandlerMap.get(getKey(serviceMeta));
    }

    public static void closeYoungConsumerHandler() {
        Collection<YoungConsumerHandler> youngConsumerHandlers = consumerHandlerMap.values();
        youngConsumerHandlers.forEach(YoungConsumerHandler::close);
        consumerHandlerMap.clear();
    }
}
