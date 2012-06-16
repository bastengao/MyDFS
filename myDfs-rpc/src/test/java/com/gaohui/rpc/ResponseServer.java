package com.gaohui.rpc;

import com.gaohui.rpc.codec.xml.xstream.XStreamInvocationCodec;
import com.thoughtworks.xstream.XStream;
import org.jboss.netty.bootstrap.ServerBootstrap;
import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.buffer.ChannelBuffers;
import org.jboss.netty.channel.*;
import org.jboss.netty.channel.socket.nio.NioServerSocketChannelFactory;

import java.net.InetSocketAddress;
import java.util.concurrent.Executors;

/**
 * User: Administrator
 * Date: 11-1-15 Time: 下午6:29
 *
 * @author Basten Gao
 */
public class ResponseServer {

    public static void main(String[] args) {
        ServerBootstrap serverBootstrap = new ServerBootstrap(new NioServerSocketChannelFactory(Executors.newCachedThreadPool(), Executors.newCachedThreadPool()));

        serverBootstrap.setPipelineFactory(new ChannelPipelineFactory() {
            public ChannelPipeline getPipeline() throws Exception {
                System.out.println("create pipline");
                return Channels.pipeline(new SimpleChannelUpstreamHandler() {
                    @Override
                    public void messageReceived(ChannelHandlerContext ctx, MessageEvent e) throws Exception {
                        System.out.println("received");
                        ChannelBuffer buffer = (ChannelBuffer) e.getMessage();
                        byte[] bytes = new byte[10000];
                        for (int i = 0; buffer.readable(); i++) {
                            bytes[i] = buffer.readByte();
                        }
                        System.out.println(new String(bytes));
                        Invocation invocation = new XStreamInvocationCodec().decode(bytes);

                        Parameter para = invocation.getParameters().get(0);

                        String response = "hello," + para.getData();
                        e.getChannel().write(ChannelBuffers.wrappedBuffer(new XStream().toXML(response).getBytes()));
                    }
                });
            }
        });

        serverBootstrap.bind(new InetSocketAddress(8888));
    }
}
