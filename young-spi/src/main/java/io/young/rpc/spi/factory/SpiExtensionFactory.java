package io.young.rpc.spi.factory;

import io.young.rpc.spi.annotation.SPIClass;
import io.young.rpc.spi.loader.ExtensionLoader;

import java.util.Optional;

/**
 * @author YoungCR
 * @date 2025/3/26 13:42
 * @descritpion SpiExtensionFactory
 */
@SPIClass
public class SpiExtensionFactory implements ExtensionFactory {

    @Override
    public <T> T getExtension(final String key, final Class<T> type) {
        return Optional.ofNullable(type)
                       .filter(Class::isInterface)
                       .filter(cls -> cls.isAnnotationPresent(SPIClass.class))
                       .map(ExtensionLoader::getExtensionLoader)
                       .map(ExtensionLoader::getDefaultSpiClassInstance)
                       .orElse(null);
    }
}
