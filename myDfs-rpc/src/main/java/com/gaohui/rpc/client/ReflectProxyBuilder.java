package com.gaohui.rpc.client;

/**
 * 通过反射得到的代理类
 * User: Administrator
 * Date: 11-1-9 Time: 下午11:48
 *
 * @author Basten Gao
 */
public class ReflectProxyBuilder implements ProxyBuilder {
    @Override
    public Object createProxy(Class<?> clazz, MethodInterceptor methodInterceptor) {
        return ReflectMethodInterceptor.createProxy(clazz, methodInterceptor);
    }
}
