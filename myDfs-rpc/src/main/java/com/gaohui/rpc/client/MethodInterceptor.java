package com.gaohui.rpc.client;

import java.lang.reflect.Method;

/**
 * 此类的并没有提供方法拦截的相应方法，是因为方法拦截不是系统来完成。
 * <P>比如通过动态代理的方法拦截是jvm来完成的拦截。
 * <P>所以，不同的拦截器，没有强制实现某个方法，拦截器实现自己的拦截方式即可，方法被拦截后，只需要将拦截的方法通知MethodHandler
 * User: Administrator
 * Date: 11-1-8 Time: 下午11:30
 *
 * @author Basten Gao
 */
public interface MethodInterceptor {

    Object invoke(Object object, Method method, Object[] parameters);
}
