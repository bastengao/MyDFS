package com.gaohui.rpc.client;

import java.lang.reflect.Method;

/**
 * User: Administrator
 * Date: 11-2-1 Time: 下午6:44
 *
 * @author Basten Gao
 */
public abstract class MethodInterceptorChain implements MethodInterceptor {

    private MethodInterceptor methodInterceptor;

    protected MethodInterceptorChain(MethodInterceptor methodInterceptor) {
        this.methodInterceptor = methodInterceptor;
    }

    protected Object next(Object proxy, Method method, Object[] params) {
        return methodInterceptor.invoke(proxy, method, params);
    }
}
