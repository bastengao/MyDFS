package com.gaohui.rpc.server;

import com.gaohui.rpc.Invocation;
import com.gaohui.rpc.ReturnValue;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * //TODO 给此类添加一个方法执行器，负责调用被代理的类
 * User: Administrator
 * Date: 11-1-16 Time: 上午11:58
 *
 * @author Basten Gao
 */
public class DefaultServerServiceProxy implements ServerServiceProxy {

    //被代理的服务
    private Object service;
    //被代理的服务名
    private String serviceName;
    // methodName:String ==> methods:Set<method:Method>
    private Map<String, Set<Method>> methodMap;

    public DefaultServerServiceProxy(Object service, String serviceName) {
        this.service = service;
        this.serviceName = serviceName;
        init();
    }

    @Override
    public ReturnValue invoke(Invocation invocation) {
        Method method = getMethod(invocation);
        if (method == null) {
            throw new IllegalStateException("method is not found. method: " + invocation.getMethodName());
        }
        Object[] params = new Object[invocation.getParameters().size()];
        for (int i = 0; i < invocation.getParameters().size(); i++) {
            params[i] = invocation.getParameters().get(i).getData();
        }
        try {
            Object data = method.invoke(service, params);
            return new ReturnValue(method.getReturnType(), data);
        } catch (IllegalAccessException e) {
            e.printStackTrace(); //TODO
        } catch (InvocationTargetException e) {
            e.printStackTrace(); //TODO
        }
        return null; //TODO
    }

    @Override
    public Object getService() {
        return service;
    }

    @Override
    public String getServiceName() {
        return serviceName;
    }


    /**
     * 获取取请求匹配的方法
     *
     * @param invocation
     * @return
     */
    private Method getMethod(Invocation invocation) {
        Set<Method> sameNameMethods = methodMap.get(invocation.getMethodName()); //找出同名的方法
        for (Method method : sameNameMethods) {
            if (isMatch(method, invocation)) {
                return method;
            }
        }
        return null;
    }

    /**
     * 查看参数是否匹配
     *
     * @param method
     * @param invocation
     * @return
     */
    private boolean isMatch(Method method, Invocation invocation) {
        Class[] paramTypes = method.getParameterTypes();
        int count = 0;
        for (int i = 0; i < paramTypes.length && i < invocation.getParameters().size(); i++) {
            if (!paramTypes[i].isAssignableFrom(invocation.getParameters().get(i).getType())) {
                break;
            }
            ++count;
        }
        return count == invocation.getParameters().size() && count == paramTypes.length;
    }

    /**
     * 分析服务对象的方法，将方法信息放入methodMap
     */
    private void init() {
        Class clazz = service.getClass();
        Method[] allMethods = clazz.getMethods();
        methodMap = new HashMap<String, Set<Method>>();
        for (Method method : allMethods) {
            Set<Method> sameNameMethods = methodMap.get(method.getName());
            if (sameNameMethods == null) {
                sameNameMethods = new HashSet<Method>();
                methodMap.put(method.getName(), sameNameMethods);
            }
            sameNameMethods.add(method);
        }
    }
}
