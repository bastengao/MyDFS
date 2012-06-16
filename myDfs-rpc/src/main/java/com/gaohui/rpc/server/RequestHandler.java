package com.gaohui.rpc.server;

/**
 * 请求处理器
 * User: Administrator
 * Date: 11-1-16 Time: 下午4:14
 *
 * @author Basten Gao
 */
public interface RequestHandler {
    /**
     * @param data 请求数据
     * @return 返回数据
     */
    byte[] request(byte[] data);
}
