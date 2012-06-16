package com.gaohui.rpc.client;

import com.gaohui.rpc.client.transport.Transportor;
import com.gaohui.rpc.client.transport.netty.NettyTransportor;
import com.gaohui.rpc.codec.InvocationCodec;
import com.gaohui.rpc.codec.ReturnValueCodec;
import com.gaohui.rpc.codec.xml.xstream.XStreamInvocationCodec;
import com.gaohui.rpc.codec.xml.xstream.XStreamReturnValueCodec;

import java.net.SocketAddress;

/**
 * User: Administrator
 * Date: 11-1-9 Time: 下午10:57
 *
 * @author Basten Gao
 */
public class DefaultClientServiceFactory implements ClientServiceFactory {
    private ProxyBuilder proxyBuilder = new ReflectProxyBuilder();
    private InvocationCodec dataCodec = new XStreamInvocationCodec();
    private ReturnValueCodec returnValueCodec = new XStreamReturnValueCodec();
    private SocketAddress address = null;

    public DefaultClientServiceFactory(SocketAddress address) {
        this.address = address;
    }

    @Override
    public <T> T createClientService(Class<T> clazz, String serviceName) {
        Object proxy = proxyBuilder.createProxy(clazz, createMethodInterceptor(clazz, serviceName));
        return clazz.cast(proxy);
    }

    private MethodInterceptor createMethodInterceptor(Class clazz, String serviceName) {
        Transportor transportor = new NettyTransportor(address);
        ClientServiceProxyDelegateMethodHandler clientServiceProxy = new ClientServiceProxyDelegateMethodHandler(serviceName, new DefaultClientServiceProxy(dataCodec, returnValueCodec, transportor));
        return new SessionProtocol(new MethodInterceptorWrapper(clientServiceProxy), transportor, serviceName);
    }
}
