package com.gaohui.rpc.client;

import com.gaohui.rpc.Invocation;
import com.gaohui.rpc.ReturnValue;

/**
 * 客户端的远程服务代理
 * User: Administrator
 * Date: 11-1-8 Time: 下午10:29
 *
 * @author Basten Gao
 */
public interface ClientServiceProxy {
    ReturnValue invoke(Invocation invocation);
}
