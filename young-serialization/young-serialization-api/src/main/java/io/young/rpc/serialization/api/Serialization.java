package io.young.rpc.serialization.api;

/**
 * @author YoungCR
 * @date 2024/12/23 20:21
 * @descritpion Serialiazation
 */
public interface Serialization {

    /**
     * 序列化
     *
     * @param obj 对象
     * @return 序列化后的字节数组
     */
    <T> byte[] serialize(T obj);

    /**
     * 反序列化
     *
     * @param data 数据
     * @param clazz 目标类
     * @return 反序列化后的对象
     */
    <T> T deserialize(byte[] data, Class<T> clazz);
}
