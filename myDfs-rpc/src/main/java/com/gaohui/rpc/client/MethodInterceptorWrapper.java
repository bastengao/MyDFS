package com.gaohui.rpc.client;

import java.lang.reflect.Method;

/**
 * 将拦截的方法转发给MethodHandler。
 * User: Administrator
 * Date: 11-1-9 Time: 上午12:01
 *
 * @author Basten Gao
 */
public class MethodInterceptorWrapper implements MethodInterceptor {
    private final MethodHandler methodHandler;

    public MethodInterceptorWrapper(MethodHandler methodHandler) {
        this.methodHandler = methodHandler;
    }

    @Override
    public Object invoke(Object object, Method method, Object[] parameters) {
        return methodHandler.methodInvoke(object, method, parameters);
    }

}
