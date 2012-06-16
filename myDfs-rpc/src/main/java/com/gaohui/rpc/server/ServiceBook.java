package com.gaohui.rpc.server;

/**
 * User: Administrator
 * Date: 11-1-5 Time: 下午4:42
 *
 * @author Bastengao
 */
public interface ServiceBook {
    <T> void register(Class<T> serviceInterface, T serviceImpl);

    <T> void register(String serviceName, Class<T> serviceInterface, T serviceImple);
}
