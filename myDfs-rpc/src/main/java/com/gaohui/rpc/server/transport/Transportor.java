package com.gaohui.rpc.server.transport;

/**
 * 负责数据传输的类，不同的传输层实现需要通过自己独特的实现，来通过此类来通知数据的接收。同时此类担任数据的发送任务。
 * User: Administrator
 * Date: 11-1-17 Time: 下午10:08
 *
 * @author Basten Gao
 */
public interface Transportor {
    /**
     * 不同的实现会调用此类，通知接收的数据
     *
     * @param data
     */
    void received(byte[] data);

    /**
     * 调用此方法可以完成数据的发送，通常供上层服务调用。
     *
     * @param data
     */
    void send(byte[] data);
}
