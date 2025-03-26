package io.young.rpc.codec;

import io.young.rpc.serialization.api.Serialization;
import io.young.rpc.serialization.jdk.JdkSerialization;

/**
 * @author YoungCR
 * @date 2024/12/23 20:52
 * @descritpion YoungCodec 编解码器接口
 */
public interface YoungCodec {

    /**
     * 获取Jdk序列化实例
     * @return Jdk序列化实例
     */
    default Serialization getJdkSerialization() {
        return new JdkSerialization();
    }
}
