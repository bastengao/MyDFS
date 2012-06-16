package com.gaohui.myDfs.transfer;

import org.jboss.netty.bootstrap.ServerBootstrap;
import org.jboss.netty.channel.Channel;

/**
 * User: Administrator
 * Date: 11-4-1 Time: 上午12:46
 *
 * @author Basten Gao
 */
public class DefaultCloseCallback implements CloseCallback {
    private Channel serverChannel = null;
    private ServerBootstrap serverBootstrap = null;

    @Override
    public void close() {
        serverChannel.close().awaitUninterruptibly();
        serverBootstrap.releaseExternalResources();
    }


    @Override
    public void setServerChannel(Channel channel) {
        this.serverChannel = channel;
        System.out.println();
    }
    @Override
    public void setServerBootstrap(ServerBootstrap serverBootstrap) {
        this.serverBootstrap = serverBootstrap;
    }
}
