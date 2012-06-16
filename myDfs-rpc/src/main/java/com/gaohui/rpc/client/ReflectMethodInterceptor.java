package com.gaohui.rpc.client;

import com.gaohui.rpc.Closable;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * 通过java的动态代理来实现方法的拦截
 * User: Administrator
 * Date: 11-1-8 Time: 下午10:49
 *
 * @author Basten Gao
 */
public class ReflectMethodInterceptor implements InvocationHandler {
    private MethodInterceptor methodInterceptor;

    public ReflectMethodInterceptor(MethodInterceptor methodInterceptor) {
        this.methodInterceptor = methodInterceptor;
    }

    public static <T> T createProxy(Class<T> t, MethodInterceptor methodInterceptor) {
        Object serviceProxy = Proxy.newProxyInstance(t.getClassLoader(), new Class[]{t, Closable.class}, new ReflectMethodInterceptor(methodInterceptor));
        return t.cast(serviceProxy);
    }

    /**
     * @param proxy
     * @param method
     * @param args
     * @return
     * @throws Throwable
     */
    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        return methodInterceptor.invoke(proxy, method, args);
    }
}
