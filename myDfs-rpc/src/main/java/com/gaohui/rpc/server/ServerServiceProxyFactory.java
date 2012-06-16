package com.gaohui.rpc.server;

/**
 * 服务器端服务代理工厂
 * User: Administrator
 * Date: 11-1-16 Time: 下午8:57
 *
 * @author Basten Gao
 */
public interface ServerServiceProxyFactory {
    /**
     * 根据给定的服务名，创建服务代理
     *
     * @param serviceName
     * @return
     */
    ServerServiceProxy createProxy(String serviceName);

    /**
     * 查看是否有此服务对象
     *
     * @param serviceName
     * @return
     */
    boolean hasService(String serviceName);
}
