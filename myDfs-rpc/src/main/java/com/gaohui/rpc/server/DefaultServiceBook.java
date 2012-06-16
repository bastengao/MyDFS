package com.gaohui.rpc.server;

import com.gaohui.rpc.server.netty.NettyServer;

import java.net.SocketAddress;

/**
 * User: Administrator
 * Date: 11-1-16 Time: 下午3:01
 *
 * @author Basten Gao
 */
public class DefaultServiceBook implements ServiceBook {
    private SingletonServiceProvider serviceProvider = new SingletonServiceProvider();
    private Server server = null;

    public DefaultServiceBook(SocketAddress socketAddress) {
        server = new NettyServer(socketAddress, new DefaultServerServiceProxyFactory(serviceProvider));
    }

    @Override
    public <T> void register(Class<T> serviceInterface, T serviceImpl) {
        serviceProvider.register(serviceInterface.getName(), serviceImpl);
    }

    @Override
    public <T> void register(String serviceName, Class<T> serviceInterface, T serviceImple) {
        serviceProvider.register(serviceName, serviceImple);
    }
}
