package com.gaohui.rpc;

import java.util.Collections;
import java.util.List;

/**
 * 表示 Client 的调用。
 * <p> Immutable
 * <p> 因为此类是不可变的，一旦构造完成，此类是不可变的。有时候可以利用Builder创建Invocation。
 * <pre>
 * Invocation invocation=Invocation.newBuilder()
 *                              .setTargetService(service)
 *                              .setMethodName(methodName)
 *                              .setParameter(parameters)
 *                              .setToken(tokent)
 *                              .build();
 * </pre>
 * User: Administrator
 * Date: 11-1-5 Time: 下午4:30
 *
 * @author Bastengao
 */
public class Invocation {
    private final Token token;
    private final Service service;
    private final String method;
    private final List<Parameter> params;

    /**
     * 构造方法。
     *
     * @param token
     * @param service
     * @param method
     * @param params
     * @throws NullPointerException 参数为null时
     */
    public Invocation(Token token, Service service, String method, List<Parameter> params) {
        if (token == null || service == null || method == null || params == null) {
            throw new NullPointerException("all parameters of constructure should not be null ");
        }
        this.token = token;
        this.service = service;
        this.method = method;
        this.params = Collections.unmodifiableList(params);
    }

    /**
     * 用户的标识
     *
     * @return
     */
    public Token getToken() {
        return token;
    }

    /**
     * 远程服务的标识
     *
     * @return
     */
    public Service getTargetService() {
        return service;
    }

    /**
     * 远程方法的标识
     *
     * @return
     */
    public String getMethodName() {
        return method;
    }

    /**
     * 输入参数
     *
     * @return
     */
    public List<Parameter> getParameters() {
        return params;
    }

    /**
     * 返回构造器
     *
     * @return
     */
    public static Builder newBuilder() {
        return new Builder();
    }


    /**
     * Invocation的构造类。方便创建不可变的Invocation对象。
     */
    public static class Builder {

        private Service service;
        private String method;
        private Token token;
        private List<Parameter> params;

        public Builder() {
        }

        public Builder setTargetService(Service service) {
            this.service = service;
            return this;
        }

        public Builder setMethodName(String methodName) {
            this.method = methodName;
            return this;
        }

        public Builder setToken(Token token) {
            this.token = token;
            return this;
        }

        public Builder setParameter(List<Parameter> params) {
            this.params = params;
            return this;
        }

        public Invocation build() {
            return new Invocation(token, service, method, params);
        }
    }
}
