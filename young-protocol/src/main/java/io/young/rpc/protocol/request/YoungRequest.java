package io.young.rpc.protocol.request;

import io.young.rpc.protocol.base.BaseMessage;

import java.io.Serial;

/**
 * @author YoungCR
 * @date 2024/12/23 13:35
 * @descritpion YoungRequest q
 */
public class YoungRequest extends BaseMessage {


    @Serial
    private static final long serialVersionUID = 4040795203578193711L;

    /**
     * 类名称
     */
    private String className;

    /**
     * 方法名称
     */
    private String methodName;

    /**
     * 参数类型数组
     */
    private Class<?>[] parameterTypes;

    /**
     * 参数数组
     */
    private Object[] parameters;

    /**
     * 版本号
     */
    private String version;

    /**
     * 服务分组
     */
    private String group;

    public String getClassName() {
        return className;
    }

    public YoungRequest className(String className) {
        this.className = className;
        return this;
    }

    public String getMethodName() {
        return methodName;
    }

    public YoungRequest methodName(String methodName) {
        this.methodName = methodName;
        return this;
    }

    public Class<?>[] getParameterTypes() {
        return parameterTypes;
    }

    public YoungRequest parameterTypes(Class<?>[] parameterTypes) {
        this.parameterTypes = parameterTypes;
        return this;
    }

    public Object[] getParameters() {
        return parameters;
    }

    public YoungRequest parameters(Object[] parameters) {
        this.parameters = parameters;
        return this;
    }

    public String getVersion() {
        return version;
    }

    public YoungRequest version(String version) {
        this.version = version;
        return this;
    }

    public String getGroup() {
        return group;
    }

    public YoungRequest group(String group) {
        this.group = group;
        return this;
    }

    public YoungRequest async(boolean async) {
        setAsync(async);
        return this;
    }

    public YoungRequest oneway(boolean oneway) {
        setOneway(oneway);
        return this;
    }
}
