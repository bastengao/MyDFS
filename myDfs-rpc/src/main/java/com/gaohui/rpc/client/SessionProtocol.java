package com.gaohui.rpc.client;

import com.gaohui.rpc.Closable;
import com.gaohui.rpc.client.transport.RequestFuture;
import com.gaohui.rpc.client.transport.Transportor;

import java.lang.reflect.Method;

/**
 * 会话协议。负责会话期间的协议，在连接建立后，询问服务端服务可用否。服务可用后，转发客户端服务方法调用。服务关闭时，关闭连接。
 * User: Administrator
 * Date: 11-2-1 Time: 下午6:35
 *
 * @author Basten Gao
 */
public class SessionProtocol extends MethodInterceptorChain {
    private final Transportor transportor;

    public SessionProtocol(MethodInterceptor methodInterceptor, Transportor transportor, String serviceName) {
        super(methodInterceptor);
        this.transportor = transportor;
        sayHello(serviceName);
    }

    /**
     * 试探服务端服务是否可用
     *
     * @param serviceName
     */
    private void sayHello(String serviceName) {
        RequestFuture future = transportor.request(serviceName.getBytes());
        String response = new String(future.getResponse());
        if (!response.equals("hello")) {
            throw new IllegalStateException("the service is unavailable. service: " + serviceName);
        }
    }

    /**
     * 关闭连接
     */
    public void close() {
        transportor.close();
    }

    @Override
    public Object invoke(Object object, Method method, Object[] parameters) {
        if (method.getDeclaringClass() == Closable.class) { //如果调用了Closable的close方法，那么将关闭本次会话
            if (method.getName().equals("close")) {
                close();
                return null;
            } else {
                throw new IllegalStateException("this won't happen.");
            }
        } else {
            return next(object, method, parameters);
        }
    }
}
