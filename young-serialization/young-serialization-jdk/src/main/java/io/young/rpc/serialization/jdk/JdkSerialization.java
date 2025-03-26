package io.young.rpc.serialization.jdk;

import io.young.rpc.common.exception.ExceptionFactory;
import io.young.rpc.common.exception.SerializerException;
import io.young.rpc.common.util.RespUtils;
import io.young.rpc.serialization.api.Serialization;

import java.io.*;

/**
 * @author YoungCR
 * @date 2024/12/23 20:24
 * @descritpion JdkSerialization JDK序列化
 */
public class JdkSerialization implements Serialization {

    @Override
    public <T> byte[] serialize(T obj) {
        if (obj == null) {
            throw ExceptionFactory.createException(SerializerException.class, RespUtils.getContent("Serializer.0"));
        }
        try {
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            ObjectOutputStream    oos = new ObjectOutputStream(bos);
            oos.writeObject(obj);
            return bos.toByteArray();
        } catch (IOException e) {
            throw ExceptionFactory.createException(SerializerException.class, e.getMessage(), e);
        }
    }

    @Override
    public <T> T deserialize(byte[] data, Class<T> clazz) {
        if (data == null) {
            throw ExceptionFactory.createException(SerializerException.class, RespUtils.getContent("Serializer.1"));
        }
        try {
            ByteArrayInputStream bis = new ByteArrayInputStream(data);
            ObjectInputStream    ois = new ObjectInputStream(bis);
            return clazz.cast(ois.readObject());
        } catch (Exception e) {
            throw ExceptionFactory.createException(SerializerException.class, e.getMessage(), e);
        }
    }
}
