package com.gaohui.myDfs.transfer;

import com.gaohui.myDfs.core.Chunk;
import com.google.common.base.Preconditions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.OutputStream;
import java.net.SocketAddress;
import java.util.ArrayList;
import java.util.List;

/**
 * 负责响应写请求
 * User: Administrator
 * Date: 11-3-30 Time: 下午9:16
 *
 * @author Basten Gao
 */
public class WriteHandler {
    private static final Logger LOGGER = LoggerFactory.getLogger(WriteHandler.class);
    private ChunkHandler chunkHandler;
    private StreamLineWriter nextSender;
    private final Transporter transporter;
    private volatile boolean connected = false;
    private volatile boolean buildStreamLineDone = false;
    private volatile boolean greetDone = false;
    private volatile boolean transferDoing = false;
    private volatile boolean transferDone = false;

    private Chunk writeChunk = null; // the chunk to be written
    private OutputStream outputStream = null;


    public WriteHandler(Transporter transporter, ChunkHandler chunkHandler) {
        this.transporter = transporter;
        this.chunkHandler = chunkHandler;
        connected();
    }

    private void connected() {
        LOGGER.trace("connected");
        connected = true;
    }

    private void buildStreamLine(List<SocketAddress> nodes) {
        LOGGER.debug("buildStreamLine:{}", nodes);
        nextSender = new NettyStreamLineWriter();
        try {
            nextSender.connectStreamLine(nodes);
            transporter.writeBack(Message.createOkMessage());
        } catch (IOException e) {
            e.printStackTrace(); //TODO
        }
    }

    private void greet(Message message) {
        LOGGER.debug("greet:{}", message);
        Preconditions.checkArgument(message.getValue() != null, "the value of message must be not null.");
        // check chunk exists
        writeChunk = (Chunk) message.getValue();
        greetDone = true;
        if (!chunkHandler.exists(writeChunk)) {
            if (nextSender != null) {
                nextSender.greet(writeChunk);
                nextSender.switchToStreamMode();
            }
            transporter.writeBack(Message.createOkMessage());
            transferDoing = true;
            transporter.switchUpToStreamMode();

            outputStream = chunkHandler.writeChunk(writeChunk);
        } else {
            transporter.writeBack(Message.createNoMessage());
            transporter.close();
            if (nextSender != null) {
                nextSender.close();
            }
        }
    }

    public void messageReceived(byte[] data) {
        LOGGER.trace("messageReceived");
        dispatch(data);
    }

    private void dispatch(byte[] data) {
        LOGGER.trace("dispatch");
        if (!transferDoing && !transferDone) { //如果连接还未建立，握手还未完成，不在传输
            Message message = null;
            message = (Message) Objects.readObject(data);
            LOGGER.trace("receivedMessage:{}", message);
            if (!buildStreamLineDone) {
                if (message.getMessageType() == Message.MessageType.BUILD_STREAM_LINE) {
                    List<SocketAddress> nodes = (List<SocketAddress>) message.getValue();
                    if (nodes.size() > 1) {
                        buildStreamLine(new ArrayList<SocketAddress>(nodes.subList(1, nodes.size())));
                    }
                    transporter.writeBack(Message.createOkMessage());
                    buildStreamLineDone = true;
                }
            } else if (buildStreamLineDone && !greetDone) {
                greet(message);
            }
        } else if (transferDoing) {
            LOGGER.trace("transferring...");
            receivedDate(data);
        }
    }


    private void receivedDate(byte[] data) {
        LOGGER.trace("receivedData:{}", data);
        if (nextSender != null) {
            nextSender.send(data);
        }
        try {
            outputStream.write(data);
        } catch (IOException e) {
            e.printStackTrace(); //TODO
        }
    }

    public void close() {
        try {
            if (outputStream != null) {
                outputStream.close();
            }
            if (nextSender != null) {
                nextSender.close();
            }
        } catch (IOException e) {
            e.printStackTrace(); //TODO
        }
    }
}
