package com.gaohui.rpc.client;

/**
 * 客户端服务工厂
 * User: Administrator
 * Date: 11-1-9 Time: 下午10:46
 *
 * @author Basten Gao
 */
public interface ClientServiceFactory {
    <T> T createClientService(Class<T> clazz, String serviceName);
}
