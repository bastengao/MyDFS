package com.gaohui.myDfs.transfer;

import org.jboss.netty.bootstrap.ServerBootstrap;
import org.jboss.netty.channel.Channel;

/**
 * User: Administrator
 * Date: 11-4-1 Time: 上午12:45
 *
 * @author Basten Gao
 */
public interface CloseCallback {
    void close();

    void setServerChannel(Channel channel);

    void setServerBootstrap(ServerBootstrap serverBootstrap);
}