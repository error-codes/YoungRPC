package io.young.rpc.annotation;

import org.springframework.beans.factory.annotation.Autowired;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author YoungCR
 * @date 2024/12/16 10:21
 * @descritpion YoungReference 服务消费者
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Autowired
public @interface YoungReference {

    /**
     * 注册中心类型，目前包括：zookeeper、nacos、etcd、consul
     */
    String registryType() default "zookeeper";

    /**
     * 注册地址
     */
    String registryAddress() default "127.0.0.1:2181";

    /**
     * 负载均衡策略，默认基于ZK的一致性HASH
     */
    String loadBalance() default "zkconsistenthash";

    /**
     * 序列化类型，目前包括：protostuff、kryo、json、jdk、hessian2、fst
     */
    String serializationType() default "protostuff";

    /**
     * 超时时间
     */
    long timeout() default 5000;

    /**
     * 是否异步执行
     */
    boolean async() default false;

    /**
     * 是否单向调用
     */
    boolean oneway() default false;

    /**
     * 代理的类型，目前包括：jdk、javassist、cglib
     */
    String proxy() default "jdk";

    /**
     * 版本号
     */
    String version() default "1.0.0";

    /**
     * 服务分组
     */
    String group() default "";
}
