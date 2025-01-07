package io.young.rpc.provider;

import io.young.rpc.common.scanner.server.YoungServiceScanner;
import io.young.rpc.provider.common.handler.YoungProviderHandler;
import io.young.rpc.provider.common.server.base.BaseServer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author YoungCR
 * @date 2024/12/19 20:53
 * @descritpion YoungSingleServer
 */
public class YoungSingleServer extends BaseServer {

    private final Logger logger = LoggerFactory.getLogger(YoungSingleServer.class);

    public YoungSingleServer(String serverAddress, String scanPackage, String reflectType) {
        super(serverAddress, reflectType);

        this.handlerMap = YoungServiceScanner.doScannerWithRpcServiceAnnotationFilterAndRegistryService(scanPackage);
    }
}
