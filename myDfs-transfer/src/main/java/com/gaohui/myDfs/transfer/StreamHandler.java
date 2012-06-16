package com.gaohui.myDfs.transfer;

import com.google.common.base.Preconditions;
import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.buffer.ChannelBuffers;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelFuture;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.ChannelStateEvent;
import org.jboss.netty.channel.MessageEvent;
import org.jboss.netty.channel.SimpleChannelHandler;
import org.jboss.netty.handler.stream.ChunkedInput;
import org.jboss.netty.handler.stream.ChunkedWriteHandler;
import org.jboss.netty.logging.InternalLoggerFactory;
import org.jboss.netty.logging.Slf4JLoggerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

/**
 * User: Administrator
 * Date: 11-3-3 Time: 下午3:46
 *
 * @author Basten Gao
 */
public class StreamHandler extends SimpleChannelHandler {
    static {
        InternalLoggerFactory.setDefaultFactory(new Slf4JLoggerFactory());
    }

    private static final Logger LOGGER = LoggerFactory.getLogger(StreamHandler.class);
    private ChunkHandler chunkHandler;
    private WriteHandler writeHandler = null;
    private ReadHandler readHandler = null;
    private Transporter transporter;
    private volatile boolean write = false;
    private volatile boolean read = false;
    private CloseCallback closeCallback;
    private Channel channel;

    public StreamHandler(CloseCallback closeCallback, ChunkHandler chunkHandler) {
        this.closeCallback = closeCallback;
        this.chunkHandler = chunkHandler;
    }

    @Override
    public void channelConnected(ChannelHandlerContext ctx, ChannelStateEvent e) throws Exception {
        LOGGER.debug("channelConnected");
        channel = e.getChannel();
        transporter = buildTransporter();
        connected();
    }

    private Transporter buildTransporter() {
        return new MyTransporter();
    }

    @Override
    public void messageReceived(ChannelHandlerContext ctx, MessageEvent e) throws Exception {
        LOGGER.trace("messageReceived");
        ChannelBuffer buffer = (ChannelBuffer) e.getMessage();
        byte[] bytes = new byte[buffer.readableBytes()];
        buffer.readBytes(bytes, 0, bytes.length);
        LOGGER.trace("received bytes:{}", bytes.length);
        dispatch(bytes);
    }

    @Override
    public void channelDisconnected(ChannelHandlerContext ctx, ChannelStateEvent e) throws Exception {
        if (write) {
            writeHandler.close();
        }
    }

    private void dispatch(byte[] data) {
        LOGGER.debug("dispatch");
        if (!write && !read) {
            Message message = null;
            message = (Message) Objects.readObject(data);
            LOGGER.trace("receivedMessage:{}", message);
            createHandler(message);
        } else if (write) {
            writeHandler.messageReceived(data);
        } else if (read) {
            readHandler.messageReceived(data);
        }
    }

    private void connected() {
        LOGGER.debug("connected");
    }

    private void createHandler(Message message) {
        Preconditions.checkArgument(message.getOperationType() != null, "the operation type of message should not be null");

        if (message.getOperationType() == Message.OperationType.WRITE) {
            write = true;
            read = false;
            writeHandler = new WriteHandler(transporter, chunkHandler);
            try {
                send(Message.createOkMessage());
            } catch (IOException e) {
                e.printStackTrace(); //TODO
            }
        } else if (message.getOperationType() == Message.OperationType.READ) {
            write = false;
            read = true;
            readHandler = new ReadHandler(transporter, chunkHandler);
            try {
                send(Message.createOkMessage());
            } catch (IOException e) {
                e.printStackTrace(); //TODO
            }
        }
    }

    private void send(Message message) throws IOException {
        byte[] data = Objects.writeObject(message);
        send(data);
    }

    private void send(byte[] data) {
        channel.write(ChannelBuffers.wrappedBuffer(data));
    }

    private class MyTransporter implements Transporter {
        private final Logger logger = LoggerFactory.getLogger(Transporter.class);
        private Channel channel2 = channel;

        @Override
        public void writeBack(Object value) {
            logger.trace("writeBack:{}", value);
            channel2.write(ChannelBuffers.wrappedBuffer(Objects.writeObject(value)));
        }

        @Override
        public void writeBack(byte[] data) {
            logger.trace("writeBack:{}", data);
            channel2.write(ChannelBuffers.wrappedBuffer(data));
        }

        @Override
        public void writeBack(byte[] data, int i, int length) {
            logger.trace("writeBack:{}", data);
            channel2.write(ChannelBuffers.wrappedBuffer(data, i, length));
        }

        @Override
        public void writeBackAwait(byte[] data) {
            logger.trace("writeBack:{}", data);
            ChannelFuture future = channel2.write(ChannelBuffers.wrappedBuffer(data));
            future.awaitUninterruptibly();
        }

        @Override
        public void writeBackAwait(byte[] data, int i, int length) {
            logger.trace("writeBack:{}", data);
            ChannelFuture future = channel2.write(ChannelBuffers.wrappedBuffer(data, i, length));
            future.awaitUninterruptibly();
        }

        @Override
        public void switchDownToStreamMode() {
            channel2.getPipeline().remove(Constants.DOWN_FRAME_HANDLER);
            channel2.getPipeline().addBefore("handler", Constants.STREAM_HANDLER, new ChunkedWriteHandler());
        }

        @Override
        public void switchUpToStreamMode() {
            channel2.getPipeline().remove(Constants.UP_FRAME_HANDLER);
            channel2.getPipeline().addBefore("handler", Constants.STREAM_HANDLER, new ChunkedWriteHandler());
        }

        @Override
        public ChannelFuture writeChunk(ChunkedInput chunkedInput) {
            return channel2.write(chunkedInput);
        }

        @Override
        public void close() {
            logger.trace("close");
            channel2.close().awaitUninterruptibly();
        }
    }
}
