package com.gaohui.rpc.server;

import com.gaohui.rpc.Invocation;
import com.gaohui.rpc.ReturnValue;
import com.gaohui.rpc.codec.Codec;

/**
 * 服务端服务代理的基类。一个简单的适配器,请拦截的请求，转发到服务方法调用。
 * User: Administrator
 * Date: 11-1-16 Time: 下午4:11
 *
 * @author Basten Gao
 */
public class ServerServiceProxyDelegateRequestHandler implements RequestHandler {
    //Invocation的编码器
    private Codec<Invocation> invocationCodec;
    //ReturnValue的编码器
    private Codec<ReturnValue> returnValueCodec;
    //服务代理
    private ServerServiceProxy serverServiceProxy;

    protected ServerServiceProxyDelegateRequestHandler(Codec<Invocation> invocationCodec, Codec<ReturnValue> returnValueCodec, ServerServiceProxy serverServiceProxy) {
        this.invocationCodec = invocationCodec;
        this.returnValueCodec = returnValueCodec;
        this.serverServiceProxy = serverServiceProxy;
    }

    @Override
    public byte[] request(byte[] data) {
        //将请求数据解码
        Invocation invocation = invocationCodec.decode(data);
        //调用子类实现的invode方法
        ReturnValue response = serverServiceProxy.invoke(invocation);
        //将返回值编码
        byte[] returnValue = returnValueCodec.encode(response);
        return returnValue;
    }
}
