package com.gaohui.rpc.server.netty;


import com.gaohui.rpc.server.ServerServiceProxyFactory;
import com.gaohui.rpc.server.SessionProtocolRequestInterceptor;
import com.gaohui.rpc.server.transport.AbstractTransportor;
import org.jboss.netty.buffer.ChannelBuffers;
import org.jboss.netty.channel.*;

/**
 * User: Administrator
 * Date: 11-1-16 Time: 下午9:46
 *
 * @author Basten Gao
 */
public class NettyTransportor extends AbstractTransportor {
    private Channel channel;

    public NettyTransportor(Channel channel, ServerServiceProxyFactory serviceProxyFactory) {
        super(new SessionProtocolRequestInterceptor(null, serviceProxyFactory));
        this.channel = channel;
    }

    @Override
    public void send(byte[] data) {
        channel.write(ChannelBuffers.wrappedBuffer(data));
    }

}
