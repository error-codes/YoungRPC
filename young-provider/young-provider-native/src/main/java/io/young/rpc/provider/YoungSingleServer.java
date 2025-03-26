package io.young.rpc.provider;

import io.young.rpc.provider.common.scanner.YoungServiceScanner;
import io.young.rpc.provider.common.server.base.BaseServer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author YoungCR
 * @date 2024/12/19 20:53
 * @descritpion YoungSingleServer 单例服务端
 */
public class YoungSingleServer extends BaseServer {

    private static final Logger logger = LoggerFactory.getLogger(YoungSingleServer.class);

    public YoungSingleServer(String serverAddress, String registryAddress, String registryType, String scanPackage, String reflectType) {
        super(serverAddress, registryAddress, registryType, reflectType);
        this.handlerMap = YoungServiceScanner.doScannerWithRpcServiceAnnotationFilterAndRegistryService(this.host, this.port, scanPackage, registryService);
    }
}
