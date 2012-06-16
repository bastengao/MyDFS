package com.gaohui.myDfs.transfer;

import com.gaohui.myDfs.core.Chunk;
import org.jboss.netty.bootstrap.ClientBootstrap;
import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.buffer.ChannelBuffers;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelFuture;
import org.jboss.netty.channel.ChannelFutureListener;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.ChannelPipeline;
import org.jboss.netty.channel.ChannelPipelineFactory;
import org.jboss.netty.channel.ChannelStateEvent;
import org.jboss.netty.channel.Channels;
import org.jboss.netty.channel.ExceptionEvent;
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
import java.io.InputStream;
import java.net.SocketAddress;
import java.util.concurrent.Exchanger;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * User: Administrator
 * Date: 11-3-31 Time: 上午12:49
 *
 * @author Basten Gao
 */
public class NettyStreamReader extends SimpleChannelHandler implements StreamReader {
    static {
        InternalLoggerFactory.setDefaultFactory(new Slf4JLoggerFactory());
    }

    private static final Logger LOGGER = LoggerFactory.getLogger(NettyStreamReader.class);
    private Lock lock = new ReentrantLock();
    private Condition condition = lock.newCondition();
    private ClientBootstrap clientBootstrap;
    private Channel channel;
    private ChannelFuture future;
    private volatile boolean sendWannaReadDoing = false;
    private volatile boolean sendWannaReadDone = false;
    private volatile boolean greetDoing = false;
    private volatile boolean greetDone = false;
    private volatile boolean greetSucceed = false;
    private volatile boolean transferDoing = false;
    private volatile boolean transferDone = false;
    private MyInputStream inputStream = new MyInputStream();

    @Override
    public void connect(SocketAddress socketAddress) {
        doConnect(socketAddress);
        sendWannaRead();
    }

    private void doConnect(SocketAddress socketAddress) {
        clientBootstrap = new ClientBootstrap(new NioClientSocketChannelFactory(Executors.newCachedThreadPool(), Executors.newCachedThreadPool()));
        clientBootstrap.setPipelineFactory(new ChannelPipelineFactory() {
            @Override
            public ChannelPipeline getPipeline() throws Exception {
                ChannelPipeline pipeline = buildPipeline();
                setChannelHandler(pipeline);
                return pipeline;
            }
        });
        future = clientBootstrap.connect(socketAddress);
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

    private void setChannelHandler(ChannelPipeline pipeline) {
        pipeline.addLast("handler", new MySimpleChannelHandler());
    }

    private ChannelPipeline buildPipeline() {
        ChannelPipeline pipeline = Channels.pipeline();
        pipeline.addLast(Constants.LOGGING_HANDLER, new LoggingHandler());
        pipeline.addLast(Constants.UP_FRAME_HANDLER, new LengthFieldBasedFrameDecoder(Integer.MAX_VALUE, 0, 2, 0, 2));
        pipeline.addLast(Constants.DOWN_FRAME_HANDLER, new LengthFieldPrepender(2, false));
        return pipeline;
    }

    private void received(ChannelBuffer buffer) {
        dispatch(buffer);
    }

    private void dispatch(ChannelBuffer buffer) {
        LOGGER.trace("dispatch");
        if (transferDoing && !transferDone) {
            receiveData(buffer);
        } else {
            byte[] bytes = new byte[buffer.readableBytes()];
            buffer.readBytes(bytes, 0, bytes.length);
            Message message = null;
            message = (Message) Objects.readObject(bytes);
            if (sendWannaReadDoing && !sendWannaReadDone) {
                replySendWannaRead(message);
            } else if (greetDoing && !greetDone) {
                replyGreet(message);
            }
        }
    }


    private void sendWannaRead() {
        LOGGER.trace("sendWannaRead");
        Message message = new Message();
        message.setOperationType(Message.OperationType.READ);
        try {
            lock.lock();
            try {
                sendWannaReadDoing = true;
                send(message);
            } catch (IOException e) {
                e.printStackTrace(); //TODO
            }
            while (!sendWannaReadDone) {
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

    private void replySendWannaRead(Message message) {
        LOGGER.trace("replySendWannaRead:{}", message);
        try {
            lock.lock();
            sendWannaReadDoing = false;
            sendWannaReadDone = true;
            condition.signal();
        } finally {
            lock.unlock();
        }
    }

    @Override
    public void greet(Chunk chunk) {
        LOGGER.trace("greet:{}", chunk);
        Message message = new Message();
        message.setValue(chunk);
        message.setOperationType(Message.OperationType.READ);
        try {
            lock.lock();
            try {
                greetDoing = true;
                greetDone = false;
                send(message);
            } catch (IOException e) {
                e.printStackTrace(); //TODO
            }
            while (!greetDone) {
                try {
                    condition.await();
                } catch (InterruptedException e) {
                    e.printStackTrace(); //TODO
                }
            }
            if (!greetSucceed) {
                throw new IllegalStateException(String.format("greet failure: %s", chunk));
            }
        } finally {
            lock.unlock();
        }
    }

    private void replyGreet(Message message) {
        LOGGER.trace("replyGreet:{}", message);
        Message.MessageType messageType = message.getMessageType();
        try {
            lock.lock();
            if (messageType == Message.MessageType.OK) {
                //TODO
                greetSucceed = true;
            } else if (messageType == Message.MessageType.NO) {
                //TODO
                greetSucceed = false;
            }
            greetDoing = false;
            greetDone = true;

            transferDoing = true;
            transferDone = false;
            condition.signal();
        } finally {
            lock.unlock();
        }
    }

    @Override
    public InputStream openStream() {
        LOGGER.trace("openStream");
        if (greetDone && greetSucceed) {
            try {
                send(Message.createReadyMessage());
                switchToStreamMode();
            } catch (IOException e) {
                e.printStackTrace(); //TODO
            }
            return inputStream;
        }
        return null;
    }

    @Override
    public void close() {
        LOGGER.trace("close");
        future.getChannel().close().awaitUninterruptibly();
        clientBootstrap.releaseExternalResources();
        LOGGER.trace("closed");
    }

    /**
     * @param e
     */
    private void closeWithoutConnection(ExceptionEvent e) {
        //TODO  如果连接失败，正常关闭并且释放资源
        LOGGER.trace("close");
        ChannelFuture future = e.getChannel().close();
        future.addListener(new ChannelFutureListener() {
            public void operationComplete(ChannelFuture future) {
                clientBootstrap.releaseExternalResources();
                LOGGER.trace("closed");
            }
        });
    }

    private void receiveData(ChannelBuffer data) {
        LOGGER.trace("receivedData");
        inputStream.append(data);
    }

    private void send(Message message) throws IOException {
        LOGGER.trace("send Message:{}", message);
        byte[] data = Objects.writeObject(message);
        send(data);
    }

    private void send(byte[] data) {
        LOGGER.trace("send data");
        channel.write(ChannelBuffers.wrappedBuffer(data));
    }

    private void switchToStreamMode() {
        channel.getPipeline().remove(Constants.UP_FRAME_HANDLER);
        channel.getPipeline().addBefore("handler", Constants.STREAM_HANDLER, new ChunkedWriteHandler());
    }

    private class MyInputStream extends InputStream {
        private final Logger logger = LoggerFactory.getLogger(MyInputStream.class);
        private Lock lock = new ReentrantLock();
        private Condition condition = lock.newCondition();
        private ChannelBuffer buffer = ChannelBuffers.buffer(1);
        private boolean closed = false;
        private Exchanger<ChannelBuffer> exchanger = new Exchanger<ChannelBuffer>();

        private int count;

        @Override
        public int read() throws IOException {
            logger.trace("read");
            while (!buffer.readable() && !closed) {
                try {
                    logger.trace("exchanging...");
                    buffer = exchanger.exchange(buffer, 1, TimeUnit.SECONDS);
                    logger.trace("exchanged:{}", buffer);
                } catch (InterruptedException e) {
                    e.printStackTrace(); //TODO
                } catch (TimeoutException e) {
                    //do nothing.
                }
            }
            if (buffer.readable()) {
                count++;
                byte b = buffer.readByte();
                if (b < 0) {
                    return b + 256;
                }
                return (int) b;
            }
            if (closed) {
                logger.trace("return EOF");
                return -1;
            }
            logger.trace("return EOF");
            return -1;
        }

        public void append(ChannelBuffer buffer) {
            logger.trace("append");
            try {
                logger.debug("get:{}", buffer.readableBytes());
                exchanger.exchange(buffer);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        public void doClose() {
            logger.trace("doClose");
            try {
                lock.lock();
                closed = true;
                condition.signal();
            } finally {
                lock.unlock();
            }
        }

        @Override
        public void close() throws IOException {
            super.close();
            doClose();
            NettyStreamReader.this.close();
        }
    }

    private class MySimpleChannelHandler extends SimpleChannelHandler {
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
            LOGGER.trace("messageReceived");
            ChannelBuffer buffer = (ChannelBuffer) e.getMessage();
            received(buffer);
        }

        @Override
        public void channelDisconnected(ChannelHandlerContext ctx, ChannelStateEvent e) throws Exception {
            LOGGER.trace("channelDisconnected");
            inputStream.doClose();
            channel.close();
        }

        @Override
        public void exceptionCaught(ChannelHandlerContext ctx, ExceptionEvent e) throws Exception {
            if (e.getCause() instanceof java.net.ConnectException) {
                e.getCause().printStackTrace();
                NettyStreamReader.this.closeWithoutConnection(e);
            }
        }
    }
}
