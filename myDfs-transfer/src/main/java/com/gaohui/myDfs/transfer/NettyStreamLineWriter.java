package com.gaohui.myDfs.transfer;

import com.gaohui.myDfs.core.Chunk;
import com.google.common.base.Preconditions;
import org.jboss.netty.bootstrap.ClientBootstrap;
import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.buffer.ChannelBuffers;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelFuture;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.ChannelPipeline;
import org.jboss.netty.channel.ChannelPipelineFactory;
import org.jboss.netty.channel.ChannelStateEvent;
import org.jboss.netty.channel.Channels;
import org.jboss.netty.channel.MessageEvent;
import org.jboss.netty.channel.SimpleChannelHandler;
import org.jboss.netty.channel.socket.nio.NioClientSocketChannelFactory;
import org.jboss.netty.handler.codec.frame.LengthFieldBasedFrameDecoder;
import org.jboss.netty.handler.codec.frame.LengthFieldPrepender;
import org.jboss.netty.handler.logging.LoggingHandler;
import org.jboss.netty.handler.stream.ChunkedWriteHandler;
import org.jboss.netty.logging.InternalLoggerFactory;
import org.jboss.netty.logging.Slf4JLoggerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.OutputStream;
import java.net.SocketAddress;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * User: Administrator
 * Date: 11-3-3 Time: 下午3:43
 *
 * @author Basten Gao
 */
public class NettyStreamLineWriter implements StreamLineWriter {
    static {
        InternalLoggerFactory.setDefaultFactory(new Slf4JLoggerFactory());
    }


    private static final Logger LOGGER = LoggerFactory.getLogger(NettyStreamLineWriter.class);
    private Lock lock = new ReentrantLock();
    private Condition condition = lock.newCondition();
    private volatile boolean sendWannaWriteDoing = false;
    private volatile boolean sendWannaWriteDone = false;
    private volatile boolean sendStreamLineDoing = false; //正在发送？
    private volatile boolean sendStreamLinedDone = false; //已经发送了？
    private volatile boolean sendStreamLineSucceed = false; //
    private volatile boolean greetDoing = false;
    private volatile boolean greetDone = false;
    private volatile boolean greetSucceed = false;
    private Channel channel;

    private MyOutputStream outputStream = null;
    private ClientBootstrap clientBootstrap;

    /**
     * 创建流水线
     *
     * @param nodes 节点地址
     * @throws IOException if io error
     */
    @Override
    public void connectStreamLine(List<SocketAddress> nodes) throws IOException {
        LOGGER.trace("connectStreamLine:{}", nodes);
        Preconditions.checkArgument(nodes.size() > 0, "the size of nodes is 0.");

        connect(nodes.get(0));
        sendWannaWrite();
        sendStreamLineNodes(nodes);
    }

    /**
     * 告诉对应是写请求
     * 注意：此方法是阻塞的
     *
     * @throws IOException if io error
     */
    private void sendWannaWrite() throws IOException {
        LOGGER.trace("sendWannaWrite");
        sendWannaWriteDoing = true;
        Message message = new Message();
        message.setOperationType(Message.OperationType.WRITE);
        send(message);
        try {
            lock.lock();
            while (!sendWannaWriteDone) {
                try {
                    condition.await();
                } catch (InterruptedException e) {
                    e.printStackTrace(); //TODO

                }
            }
        } finally {
            lock.unlock();
        }
    }

    /**
     * 回复“想写请求”
     *
     * @param message 要发送的message
     */
    private void replySendWannaWrite(Message message) {
        LOGGER.trace("replySendWannaWrite:{}", message);
        try {
            lock.lock();
            sendWannaWriteDone = true;
            sendWannaWriteDoing = false;
            condition.signal();
        } finally {
            lock.unlock();
        }
    }

    /**
     * 连接下一个节点(nodes[0])
     * 注意:此方法在连接成功前是阻塞的
     *
     * @param address 连接的地址
     */
    private void connect(SocketAddress address) {
        LOGGER.trace("connect:{}", address);
        final Condition condition = lock.newCondition();
        clientBootstrap = new ClientBootstrap(new NioClientSocketChannelFactory(Executors.newCachedThreadPool(), Executors.newCachedThreadPool()));
        clientBootstrap.setPipelineFactory(new ChannelPipelineFactory() {
            public ChannelPipeline getPipeline() throws Exception {
                ChannelPipeline pipeline = Channels.pipeline();
                pipeline.addLast(Constants.LOGGING_HANDLER, new LoggingHandler());
                pipeline.addLast(Constants.UP_FRAME_HANDLER, new LengthFieldBasedFrameDecoder(Integer.MAX_VALUE, 0, 2, 0, 2));
                pipeline.addLast(Constants.DOWN_FRAME_HANDLER, new LengthFieldPrepender(2, false));
                pipeline.addLast("handler", new MySimpleChannelHandler(condition));
                return pipeline;
            }
        });
        ChannelFuture future = clientBootstrap.connect(address);
        lock.lock();
        try {
            while (channel == null) { //等待连接成功，并获得channel
                try {
                    condition.await();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        } finally {
            lock.unlock();
        }
    }

    /**
     * 向一个节点发送从他开始的(包括他自己的地址)流水线节点地址
     * 注意:此方法在调用完成前是阻塞的。
     *
     * @param nextNodes 发送节点地址
     */
    private void sendStreamLineNodes(List<SocketAddress> nextNodes) {
        LOGGER.trace("sendStreamLineNodes:{}", nextNodes);
        Message message = Message.createBuildStreamLineMessage(nextNodes);
        send(message);
        sendStreamLineDoing = true;
        lock.lock();
        try {
            while (!sendStreamLinedDone) {
                try {
                    condition.await();
                } catch (InterruptedException e) {
                    e.printStackTrace(); //TODO
                }
            }
        } finally {
            lock.unlock();
        }
        if (sendStreamLineSucceed) {
            //TODO
        } else {
            //TODO
        }
    }

    /**
     * 回复{@code sendStreamLineNodes(Messaga)}
     *
     * @param message 回复的message
     */
    private void replySendStreamLineNodes(Message message) {
        LOGGER.trace("replySendStreamLineNodes:{}", message);
        sendStreamLineDoing = false;
        sendStreamLinedDone = true;
        if (message.getMessageType() == Message.MessageType.OK) {
            sendStreamLineSucceed = true;
        } else if (message.getMessageType() == Message.MessageType.NO) {
            sendStreamLineSucceed = false;
        }
        lock.lock();
        try {
            condition.signal();
        } finally {
            lock.unlock();
        }
    }

    /**
     * 接受发送来的数据
     *
     * @param data 接收到的数据
     */
    private void received(byte[] data) {
        LOGGER.trace("received data");
        Message message = null;
        message = (Message) Objects.readObject(data);
        LOGGER.trace("receivedMessage:{}", message);
        dispatch(message);
    }

    /**
     * 分发接受的消息
     *
     * @param message 转发的请求
     */
    private void dispatch(Message message) {
        LOGGER.trace("dispatch:{}", message);
        if (sendWannaWriteDoing && !sendWannaWriteDone) {
            replySendWannaWrite(message);
        } else if (sendStreamLineDoing && !sendStreamLinedDone) {
            replySendStreamLineNodes(message);
        } else if (greetDoing && !greetDone) {
            receivedGreetReply(message);
        }
    }

    /**
     * 打个招呼
     *
     * @param chunk 请求的chunk
     */
    @Override
    public void greet(Chunk chunk) {
        LOGGER.trace("greet:{}", chunk);
        if (chunk == null) {
            throw new IllegalArgumentException(new NullPointerException("chunk is null"));
        }
        Message message = new Message();
        message.setOperationType(Message.OperationType.WRITE);
        message.setValue(chunk);
        // send data
        send(message);
        greetDoing = true;
        lock.lock();
        try {
            while (!greetDone) {
                try {
                    condition.await();
                } catch (InterruptedException e) {
                    e.printStackTrace(); //TODO
                }
            }
        } finally {
            lock.unlock();
        }
        if (greetSucceed) {
            //TODO
        } else {
            //TODO
        }
    }

    /**
     * 接收greet回复
     *
     * @param message 接收到的回复消息
     */
    private void receivedGreetReply(Message message) {
        LOGGER.trace("receivedGreetReply:{}", message);
        greetDoing = false;
        greetDone = true;
        try {
            lock.lock();
            if (message.getMessageType() == Message.MessageType.OK) {
                greetSucceed = true;
            } else if (message.getMessageType() == Message.MessageType.NO) {
                LOGGER.trace("greet failure:{}", message);
                greetSucceed = false;
            }
            condition.signal();
        } finally {
            lock.unlock();
        }
    }

    private void send(Message message) {
        LOGGER.trace("send Message:{}", message);
        byte[] data = Objects.writeObject(message);
        send(data);
    }

    @Override
    public void send(byte[] data) {
        LOGGER.trace("send data");
        channel.write(ChannelBuffers.wrappedBuffer(data));
    }

    @Override
    public void send(OutputStream outputStream) {
        //TODO
    }

    @Override
    public OutputStream getWriteStream() {
        LOGGER.trace("getWriteStream");
        if (greetDone && greetSucceed) {
            switchToStreamMode();
            outputStream = new MyOutputStream();
            return outputStream;
        }
        return null;
    }

    public void switchToStreamMode() {
        channel.getPipeline().remove(Constants.DOWN_FRAME_HANDLER);
        channel.getPipeline().addBefore("handler", Constants.STREAM_HANDLER, new ChunkedWriteHandler());
    }

    private void doClose() {
        channel.close().awaitUninterruptibly();
        clientBootstrap.releaseExternalResources();
    }

    @Override
    public void close() {
        //TODO
        doClose();
    }

    class MyOutputStream extends OutputStream {
        private ChannelFuture future;

        @Override
        public void write(int b) throws IOException {
            byte[] bytes = new byte[]{(byte) b};
            future = channel.write(ChannelBuffers.wrappedBuffer(bytes));
        }

        @Override
        public void close() throws IOException {
            future.awaitUninterruptibly();
            super.close();
            doClose();
        }
    }

    private class MySimpleChannelHandler extends SimpleChannelHandler {
        private final Condition condition;

        public MySimpleChannelHandler(Condition condition) {
            this.condition = condition;
        }

        @Override
        public void channelConnected(ChannelHandlerContext ctx, ChannelStateEvent e) throws Exception {
            lock.lock();
            try {
                channel = e.getChannel();
                condition.signal(); //唤醒等待线程
            } finally {
                lock.unlock();
            }
        }

        @Override
        public void messageReceived(ChannelHandlerContext ctx, MessageEvent e) throws Exception {
            ChannelBuffer buffer = (ChannelBuffer) e.getMessage();
            byte[] bytes = new byte[buffer.readableBytes()];
            buffer.readBytes(bytes, 0, bytes.length);
            received(bytes);
        }
    }
}
