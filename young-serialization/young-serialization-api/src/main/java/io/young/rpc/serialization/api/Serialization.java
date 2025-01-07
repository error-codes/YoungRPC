package io.young.rpc.serialization.api;

/**
 * @author YoungCR
 * @date 2024/12/23 20:21
 * @descritpion Serialiazation
 */
public interface Serialization {

    /**
     * 序列化
     */
    <T> byte[] serialize(T obj);

    /**
     * 反序列化
     */
    <T> T deserialize(byte[] data, Class<T> clazz);
}
