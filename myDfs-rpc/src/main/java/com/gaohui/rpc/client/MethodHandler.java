package com.gaohui.rpc.client;

import java.lang.reflect.Method;

/**
 * User: Administrator
 * Date: 11-1-8 Time: 下午11:10
 *
 * @author Basten Gao
 */
public interface MethodHandler {
    Object methodInvoke(Object proxy, Method method, Object[] parameters);
}
