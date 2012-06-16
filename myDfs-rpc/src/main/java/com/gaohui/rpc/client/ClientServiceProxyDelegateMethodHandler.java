package com.gaohui.rpc.client;

import com.gaohui.rpc.Invocation;
import com.gaohui.rpc.Parameter;
import com.gaohui.rpc.ReturnValue;
import com.gaohui.rpc.Service;
import com.gaohui.rpc.Token;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 * User: Administrator
 * Date: 11-1-8 Time: 下午11:43
 *
 * @author Basten Gao
 */
public class ClientServiceProxyDelegateMethodHandler implements MethodHandler {
    private String serviceName;
    private ClientServiceProxy clientServiceProxy;

    public ClientServiceProxyDelegateMethodHandler(String serviceName, ClientServiceProxy clientServiceProxy) {
        this.serviceName = serviceName;
        this.clientServiceProxy = clientServiceProxy;
    }

    @Override
    public Object methodInvoke(Object proxy, Method method, Object[] parameters) {
        Invocation invocation = createInvocation(method, parameters);
        ReturnValue returnValue = clientServiceProxy.invoke(invocation);
        return returnValue.getData();
    }

    private Invocation createInvocation(Method method, Object[] parameters) {
        List<Parameter> params = new ArrayList<Parameter>();
        if (parameters != null) {
            for (Object param : parameters) {
                params.add(new Parameter(param.getClass(), param));
            }
        }
        Invocation invocation = Invocation.newBuilder().setMethodName(method.getName())
                .setTargetService(new Service(serviceName))
                .setParameter(params)
                .setToken(new Token())
                .build();
        return invocation;
    }


}
