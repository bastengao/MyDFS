package com.gaohui.rpc.client.transport;

/**
 * 将客户端的请求发送给服务端
 * User: Administrator
 * Date: 11-1-15 Time: 下午4:56
 *
 * @author Basten Gao
 */
public interface Transportor {
    /**
     * 发送请求，同时返回RequestFuture
     *
     * @param data
     * @return
     */
    RequestFuture request(byte[] data);

    /**
     * 关闭连接
     */
    void close();
}
