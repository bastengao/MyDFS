package com.gaohui.rpc.server;

import com.gaohui.rpc.Invocation;
import com.gaohui.rpc.ReturnValue;
import com.gaohui.rpc.codec.Codec;
import com.gaohui.rpc.codec.xml.xstream.XStreamInvocationCodec;
import com.gaohui.rpc.codec.xml.xstream.XStreamReturnValueCodec;

/**
 * User: Administrator
 * Date: 11-1-31 Time: 下午11:11
 *
 * @author Basten Gao
 */
public class SessionProtocolRequestInterceptor extends RequestInterceptorChain {
    private Codec<Invocation> invocaitonCodec = new XStreamInvocationCodec();
    private Codec<ReturnValue> returnValueCodec = new XStreamReturnValueCodec();
    private ServerServiceProxyFactory serviceProxyFactory;
    //是否已经连接
    private volatile boolean connected = false;

    public SessionProtocolRequestInterceptor(RequestInterceptor nextRequestInterceptor, ServerServiceProxyFactory serverServiceProxyFactory) {
        super(nextRequestInterceptor);
        this.serviceProxyFactory = serverServiceProxyFactory;
    }

    public byte[] request(byte[] data) {
        if (connected) {
            return next(data);
        } else {
            return greet(data);
        }
    }

    protected synchronized byte[] greet(byte[] data) {
        String serviceName = new String(data);
        if (hasService(serviceName)) {
            nextRequestInterceptor = createRequestInterceptor(serviceName);
            connected = true;
            return "hello".getBytes();
        } else {
            return "noHello".getBytes();
        }
    }

    private RequestInterceptor createRequestInterceptor(String serviceName) {
        ServerServiceProxyDelegateRequestHandler serverServiceProxy = new ServerServiceProxyDelegateRequestHandler(invocaitonCodec, returnValueCodec, serviceProxyFactory.createProxy(serviceName));
        return new RequestInterceptorWrapper(serverServiceProxy);
    }

    private boolean hasService(String serviceName) {
        return serviceProxyFactory.hasService(serviceName);
    }
}
