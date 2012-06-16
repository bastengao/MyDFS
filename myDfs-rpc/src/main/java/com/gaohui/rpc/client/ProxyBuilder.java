package com.gaohui.rpc.client;

/**
 * 创建被代理的对象
 * User: Administrator
 * Date: 11-1-9 Time: 下午11:29
 *
 * @author Basten Gao
 */
public interface ProxyBuilder {
    Object createProxy(Class<?> clazz, MethodInterceptor methodInterceptor);
}
