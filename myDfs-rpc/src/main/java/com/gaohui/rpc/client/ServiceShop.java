package com.gaohui.rpc.client;

/**
 * 客户端服务提供者
 * User: Administrator
 * Date: 11-1-5 Time: 下午4:33
 *
 * @author Bastengao
 */
public interface ServiceShop {

    /**
     * 获取客户端，服务对象。服务名为Class全名称。
     *
     * @param t
     * @param <T>
     * @return
     */
    <T> T getService(Class<T> t);

    /**
     * 获取类型为T的客户端服务对象。
     *
     * @param t
     * @param serviceName
     * @param <T>
     * @return
     */
    <T> T getService(Class<T> t, String serviceName);

    /**
     * 根据指定服务名获取客户端服务对象。
     *
     * @param serviceName
     * @return
     */
    Object getService(String serviceName);

    /**
     * 结束客户端服务对象的使用。
     *
     * @param clientService
     */
    void returnService(Object clientService);
}
