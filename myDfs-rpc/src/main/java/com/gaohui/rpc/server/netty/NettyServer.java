package com.gaohui.rpc.server.netty;

import com.gaohui.rpc.server.DefaultServerServiceProxyFactory;
import com.gaohui.rpc.server.Server;
import com.gaohui.rpc.server.transport.Transportor;
import org.jboss.netty.bootstrap.ServerBootstrap;
import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.buffer.ChannelBuffers;
import org.jboss.netty.channel.ChannelEvent;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.ChannelPipeline;
import org.jboss.netty.channel.ChannelPipelineFactory;
import org.jboss.netty.channel.ChannelStateEvent;
import org.jboss.netty.channel.Channels;
import org.jboss.netty.channel.DownstreamMessageEvent;
import org.jboss.netty.channel.MessageEvent;
import org.jboss.netty.channel.SimpleChannelHandler;
import org.jboss.netty.channel.socket.nio.NioServerSocketChannelFactory;
import org.jboss.netty.handler.codec.frame.DelimiterBasedFrameDecoder;
import org.jboss.netty.handler.codec.frame.Delimiters;
import org.jboss.netty.handler.logging.LoggingHandler;
import org.jboss.netty.logging.InternalLoggerFactory;
import org.jboss.netty.logging.Slf4JLoggerFactory;

import java.net.SocketAddress;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * User: Administrator
 * Date: 11-1-16 Time: 下午3:25
 *
 * @author Basten Gao
 */
public class NettyServer extends Server {
    static {
        InternalLoggerFactory.setDefaultFactory(new Slf4JLoggerFactory());
    }

    public NettyServer(SocketAddress address, DefaultServerServiceProxyFactory proxyFactory) {
        super(proxyFactory);
        ServerBootstrap serverBootstrap = new ServerBootstrap(new NioServerSocketChannelFactory(Executors.newCachedThreadPool(), Executors.newCachedThreadPool()));

        serverBootstrap.setPipelineFactory(new ChannelPipelineFactory() {
            @Override
            public ChannelPipeline getPipeline() throws Exception {
                return Channels.pipeline(new LoggingHandler(), new DelimiterBasedFrameDecoder(Integer.MAX_VALUE, Delimiters.nulDelimiter()), new Handler());
            }
        });
        serverBootstrap.bind(address);
    }


    public class Handler extends SimpleChannelHandler {
        private Transportor transportor = null;
        private Executor executor = Executors.newSingleThreadExecutor(); //保证所有的操作都有同一个线程内,避免线程不安全的可能

        @Override
        public void channelConnected(ChannelHandlerContext ctx, ChannelStateEvent e) throws Exception {
            transportor = new NettyTransportor(ctx.getChannel(), NettyServer.super.proxyFactory);
        }

        @Override
        public void messageReceived(ChannelHandlerContext ctx, MessageEvent e) throws Exception {
            ChannelBuffer buffer = (ChannelBuffer) e.getMessage();
            final byte[] data = new byte[buffer.readableBytes()];
            buffer.readBytes(data, 0, buffer.readableBytes());
            executor.execute(new Runnable() {
                @Override
                public void run() {
                    transportor.received(data);
                }
            });
        }

        @Override
        public void handleDownstream(ChannelHandlerContext ctx, ChannelEvent e) throws Exception {
            if (e instanceof MessageEvent) {
                MessageEvent event = (MessageEvent) e;
                ChannelBuffer buffer = (ChannelBuffer) ((MessageEvent) e).getMessage();
                ChannelBuffer[] delimiters = Delimiters.nulDelimiter();
                ChannelBuffer[] buffers = new ChannelBuffer[delimiters.length + 1];
                buffers[0] = buffer;
                for (int i = 0; i < delimiters.length; i++) {
                    buffers[i + 1] = delimiters[i];
                }
                ChannelBuffer newBuffer = ChannelBuffers.copiedBuffer(buffers);
                e = new DownstreamMessageEvent(event.getChannel(), event.getFuture(), newBuffer, event.getRemoteAddress());
            }
            ctx.sendDownstream(e);
        }

        @Override
        public void channelDisconnected(ChannelHandlerContext ctx, ChannelStateEvent e) throws Exception {
            e.getChannel().close(); //如果对方结束了连接，那么我方也关闭连接
        }
    }
}
