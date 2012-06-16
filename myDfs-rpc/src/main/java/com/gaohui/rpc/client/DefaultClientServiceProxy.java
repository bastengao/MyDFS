package com.gaohui.rpc.client;

import com.gaohui.rpc.Invocation;
import com.gaohui.rpc.ReturnValue;
import com.gaohui.rpc.client.transport.RequestFuture;
import com.gaohui.rpc.client.transport.Transportor;
import com.gaohui.rpc.codec.Codec;

/**
 * User: Administrator
 * Date: 11-2-1 Time: 下午12:30
 *
 * @author Basten Gao
 */
public class DefaultClientServiceProxy implements ClientServiceProxy {
    private Codec<Invocation> invocatonCodec;
    private Codec<ReturnValue> returnValueCodec;
    private Transportor transportor;

    public DefaultClientServiceProxy(Codec<Invocation> invocatonCodec, Codec<ReturnValue> returnValueCodec, Transportor transportor) {
        this.invocatonCodec = invocatonCodec;
        this.returnValueCodec = returnValueCodec;
        this.transportor = transportor;
    }

    @Override
    public ReturnValue invoke(Invocation invocation) {
        byte[] requestData = invocatonCodec.encode(invocation);
        RequestFuture future = transportor.request(requestData);
        byte[] responseData = future.getResponse();
        ReturnValue result = returnValueCodec.decode(responseData);
        return result;
    }
}
