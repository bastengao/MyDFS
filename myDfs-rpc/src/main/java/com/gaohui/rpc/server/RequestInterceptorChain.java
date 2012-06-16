package com.gaohui.rpc.server;

/**
 * User: Administrator
 * Date: 11-1-31 Time: 下午11:14
 *
 * @author Basten Gao
 */
public abstract class RequestInterceptorChain implements RequestInterceptor {
    protected RequestInterceptor nextRequestInterceptor;

    protected RequestInterceptorChain(RequestInterceptor nextRequestInterceptor) {
        this.nextRequestInterceptor = nextRequestInterceptor;
    }

    protected byte[] next(byte[] data) {
        return nextRequestInterceptor.request(data);
    }
}
