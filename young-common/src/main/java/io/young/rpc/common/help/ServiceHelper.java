package io.young.rpc.common.help;

/**
 * @author YoungCR
 * @date 2024/12/24 18:34
 * @descritpion ServiceHelper
 */
public class ServiceHelper {

    /**
     * 拼接RPC服务信息
     *
     * @param serviceName 服务名称
     * @param version     版本号
     * @param group       分组
     * @return 拼接字符串Key
     */
    public static String buildServiceKey(String serviceName, String version, String group) {
        return String.join("#", serviceName, version, group);
    }
}
