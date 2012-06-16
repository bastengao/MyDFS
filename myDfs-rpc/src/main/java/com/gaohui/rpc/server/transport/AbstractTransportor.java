package com.gaohui.rpc.server.transport;

import com.gaohui.rpc.server.RequestInterceptor;

/**
 * Transportor的抽象实现。
 * User: Administrator
 * Date: 11-1-17 Time: 下午11:25
 *
 * @author Basten Gao
 */
public abstract class AbstractTransportor implements Transportor {
    protected RequestInterceptor requestInterceptor;

    protected AbstractTransportor(RequestInterceptor requestInterceptor) {
        this.requestInterceptor = requestInterceptor;
    }

    @Override
    public void received(byte[] data) {
        byte[] response = requestInterceptor.request(data);
        send(response);
    }
}
