package com.gaohui.rpc.server;

/**
 * User: Administrator
 * Date: 11-1-16 Time: 下午5:50
 *
 * @author Basten Gao
 */
public class Server {
    protected ServerServiceProxyFactory proxyFactory;

    public Server(ServerServiceProxyFactory proxyFactory) {
        this.proxyFactory = proxyFactory;
    }

}
