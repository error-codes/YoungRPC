package io.young.rpc.codec;

import io.young.rpc.serialization.api.Serialization;
import io.young.rpc.serialization.jdk.JdkSerialization;

/**
 * @author YoungCR
 * @date 2024/12/23 20:52
 * @descritpion YoungCodec
 */
public interface YoungCodec {

    default Serialization getJdkSerialization() {
        return new JdkSerialization();
    }
}
