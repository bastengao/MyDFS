package com.gaohui.rpc.server;

import com.gaohui.rpc.Invocation;
import com.gaohui.rpc.ReturnValue;

/**
 * 服务代理
 * User: Administrator
 * Date: 11-1-16 Time: 上午11:54
 *
 * @author Basten Gao
 */
public interface ServerServiceProxy {
    /**
     * 调用代理的服务方法
     *
     * @param invocation
     * @return
     */
    ReturnValue invoke(Invocation invocation);

    /**
     * 返回被代理的服务对象
     *
     * @return
     */
    Object getService();

    /**
     * 返回被代理服务的服务名
     *
     * @return
     */
    String getServiceName();
}
