package com.gaohui.rpc.server;

/**
 * User: Administrator
 * Date: 11-1-16 Time: 下午4:49
 *
 * @author Basten Gao
 */
public class RequestInterceptorWrapper implements RequestInterceptor {
    private RequestHandler requestHandler;

    public RequestInterceptorWrapper(RequestHandler requestHandler) {
        this.requestHandler = requestHandler;
    }

    @Override
    public byte[] request(byte[] data) {
        return requestHandler.request(data);
    }
}
