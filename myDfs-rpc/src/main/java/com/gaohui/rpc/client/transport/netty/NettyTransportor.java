package com.gaohui.rpc.client.transport.netty;

import com.gaohui.rpc.client.transport.RequestFuture;
import com.gaohui.rpc.client.transport.Transportor;
import org.jboss.netty.bootstrap.ClientBootstrap;
import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.buffer.ChannelBuffers;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelEvent;
import org.jboss.netty.channel.ChannelFactory;
import org.jboss.netty.channel.ChannelFuture;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.ChannelPipeline;
import org.jboss.netty.channel.ChannelPipelineFactory;
import org.jboss.netty.channel.ChannelStateEvent;
import org.jboss.netty.channel.Channels;
import org.jboss.netty.channel.DownstreamMessageEvent;
import org.jboss.netty.channel.ExceptionEvent;
import org.jboss.netty.channel.MessageEvent;
import org.jboss.netty.channel.SimpleChannelHandler;
import org.jboss.netty.channel.socket.nio.NioClientSocketChannelFactory;
import org.jboss.netty.handler.codec.frame.DelimiterBasedFrameDecoder;
import org.jboss.netty.handler.codec.frame.Delimiters;
import org.jboss.netty.handler.logging.LoggingHandler;
import org.jboss.netty.logging.InternalLoggerFactory;
import org.jboss.netty.logging.Slf4JLoggerFactory;

import java.net.ConnectException;
import java.net.NoRouteToHostException;
import java.net.SocketAddress;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * User: Administrator
 * Date: 11-1-15 Time: 下午5:45
 *
 * @author Basten Gao
 */
public class NettyTransportor implements Transportor {
    static {
        InternalLoggerFactory.setDefaultFactory(new Slf4JLoggerFactory());
    }

    private Handler handler = new Handler();
    private ChannelFactory channelFactory = null;
    private ChannelFuture channelFuture = null;

    public NettyTransportor(SocketAddress address) {
        channelFactory = new NioClientSocketChannelFactory(Executors.newCachedThreadPool(), Executors.newCachedThreadPool());
        ClientBootstrap clientBootstrap = new ClientBootstrap(channelFactory);
        clientBootstrap.setPipelineFactory(new ChannelPipelineFactory() {
            @Override
            public ChannelPipeline getPipeline() throws Exception {
                return Channels.pipeline(new LoggingHandler(), new DelimiterBasedFrameDecoder(Integer.MAX_VALUE, Delimiters.nulDelimiter()), handler);
            }
        });
        channelFuture = clientBootstrap.connect(address);
        channelFuture.awaitUninterruptibly(5, TimeUnit.SECONDS);
        if (channelFuture.isDone()) {
            if (!channelFuture.isSuccess()) { //TODO 不能在出现异常的时候，完全关闭程序
                Throwable ex = channelFuture.getCause();
                ex.printStackTrace();
            }
        }
    }

    @Override
    public RequestFuture request(byte[] data) {
        return handler.send(data);
    }

    @Override
    public void close() {
        handler.close(); //关闭本次会话的连接
        channelFuture.getChannel().getCloseFuture().awaitUninterruptibly(); //等待会话关闭结束
        channelFactory.releaseExternalResources(); //关闭ChannelFactory所持有的资源
    }

    private class Handler extends SimpleChannelHandler implements RequestFuture {
        private Channel channel;
        private Lock lock = new ReentrantLock();
        private Condition channelCondition = lock.newCondition();
        private Condition dataCondition = lock.newCondition();
        private byte[] data; //receivedData

        @Override
        public void channelConnected(ChannelHandlerContext ctx, ChannelStateEvent e) throws Exception {
            lock.lock();
            try {
                channel = ctx.getChannel();
                channelCondition.signal();
            } finally {
                lock.unlock();
            }
        }

        @Override
        public void messageReceived(ChannelHandlerContext ctx, MessageEvent e) throws Exception {
            lock.lock();
            try {
                ChannelBuffer buffer = (ChannelBuffer) e.getMessage();
                int length = buffer.readableBytes();
                data = new byte[length];
                buffer.readBytes(data, 0, length);
                dataCondition.signal();
            } finally {
                lock.unlock();
            }
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
        public void exceptionCaught(ChannelHandlerContext ctx, ExceptionEvent e) throws Exception {
            Throwable ex = e.getCause();
            if (ex instanceof NoRouteToHostException) {
                //do nothing
            } else if (ex instanceof ConnectException) {
                NettyTransportor.this.close();
            }
            ex.printStackTrace();
        }

        public RequestFuture send(byte[] data) {
            lock.lock();
            try {
                if (channel == null) {
                    channelCondition.await();
                }
                ChannelBuffer buffer = ChannelBuffers.wrappedBuffer(data);
                channel.write(buffer);
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                lock.unlock();
            }
            return this;
        }

        @Override
        public byte[] getResponse() {
            lock.lock();
            try {
                if (data == null) {
                    dataCondition.await();
                }
                return data;
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                data = null;
                lock.unlock();
            }
            return null;
        }

        public void close() {
            if (channel != null) {
                channel.close();
            }
        }
    }
}
