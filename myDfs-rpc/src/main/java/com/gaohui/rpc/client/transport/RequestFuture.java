package com.gaohui.rpc.client.transport;

/**
 * User: Administrator
 * Date: 11-1-15 Time: 下午4:59
 *
 * @author Basten Gao
 */
public interface RequestFuture {

    /**
     * 此方法会阻塞，直至请求的回复回返。
     *
     * @return
     */
    byte[] getResponse();
}
