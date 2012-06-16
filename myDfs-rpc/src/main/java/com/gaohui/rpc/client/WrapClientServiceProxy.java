package com.gaohui.rpc.client;

import com.gaohui.rpc.Invocation;
import com.gaohui.rpc.ReturnValue;

/**
 * User: Administrator
 * Date: 11-1-9 Time: 下午11:22
 *
 * @author Basten Gao
 */
public class WrapClientServiceProxy implements ClientServiceProxy {
    private ClientServiceProxy clientServiceProxy;

    public WrapClientServiceProxy(ClientServiceProxy clientServiceProxy) {
        this.clientServiceProxy = clientServiceProxy;
    }

    @Override
    public ReturnValue invoke(Invocation invocation) {
        return clientServiceProxy.invoke(invocation);
    }
}
