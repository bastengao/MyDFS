package com.gaohui.rpc.server;

/**
 * 服务提供者
 * User: Administrator
 * Date: 11-1-16 Time: 上午11:47
 *
 * @author Basten Gao
 */
public interface ServiceProvider {
    /**
     * 是否存在名为serviceName的服务
     *
     * @param serviceName
     * @return
     */
    boolean hasService(String serviceName);

    /**
     * 根据服务名，查找服务对象
     *
     * @param name
     * @return
     */
    Object getService(String name);
}
