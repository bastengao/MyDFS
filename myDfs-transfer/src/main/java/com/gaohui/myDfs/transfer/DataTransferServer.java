package com.gaohui.myDfs.transfer;

import org.jboss.netty.bootstrap.ServerBootstrap;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelPipeline;
import org.jboss.netty.channel.ChannelPipelineFactory;
import org.jboss.netty.channel.Channels;
import org.jboss.netty.channel.socket.nio.NioServerSocketChannelFactory;
import org.jboss.netty.handler.codec.frame.LengthFieldBasedFrameDecoder;
import org.jboss.netty.handler.codec.frame.LengthFieldPrepender;
import org.jboss.netty.handler.logging.LoggingHandler;
import org.jboss.netty.logging.InternalLoggerFactory;
import org.jboss.netty.logging.Slf4JLoggerFactory;

import java.net.InetSocketAddress;
import java.util.concurrent.Executors;

/**
 * User: Administrator
 * Date: 11-3-3 Time: 下午5:07
 *
 * @author Basten Gao
 */
public class DataTransferServer {
    static {
        InternalLoggerFactory.setDefaultFactory(new Slf4JLoggerFactory());
    }

    public DataTransferServer(InetSocketAddress inetSocketAddress, final ChunkHandler chunkHandler) {

        final CloseCallback closeCallback = new DefaultCloseCallback();
        final ServerBootstrap serverBootstrap = new ServerBootstrap(new NioServerSocketChannelFactory(Executors.newCachedThreadPool(), Executors.newCachedThreadPool()));

        serverBootstrap.setPipelineFactory(new ChannelPipelineFactory() {
            public ChannelPipeline getPipeline() throws Exception {
                ChannelPipeline pipeline = Channels.pipeline();

                pipeline.addLast(Constants.LOGGING_HANDLER, new LoggingHandler());
                pipeline.addLast(Constants.UP_FRAME_HANDLER, new LengthFieldBasedFrameDecoder(Integer.MAX_VALUE, 0, 2, 0, 2));
                pipeline.addLast(Constants.DOWN_FRAME_HANDLER, new LengthFieldPrepender(2, false));
                pipeline.addLast("handler", new StreamHandler(closeCallback, chunkHandler)); //TODO
                return pipeline;
            }
        });
        Channel channel = serverBootstrap.bind(inetSocketAddress);
        closeCallback.setServerChannel(channel);
        closeCallback.setServerBootstrap(serverBootstrap);
    }

}
