package com.gaohui.myDfs.transfer;

import com.gaohui.myDfs.core.Chunk;
import com.google.common.base.Preconditions;
import org.jboss.netty.channel.ChannelFuture;
import org.jboss.netty.channel.ChannelFutureListener;
import org.jboss.netty.handler.stream.ChunkedStream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.InputStream;

/**
 * 负责响应读请求
 * User: Administrator
 * Date: 11-3-30 Time: 下午9:16
 *
 * @author Basten Gao
 */
public class ReadHandler {
    private static final Logger LOGGER = LoggerFactory.getLogger(ReadHandler.class);
    private ChunkHandler chunkHandler = null;
    private final Transporter transporter;
    private volatile boolean greetDoing = false;
    private volatile boolean greetDone = false;
    private volatile boolean greetSucceed = false;
    private volatile boolean transferDoing = false;
    private volatile boolean transferDone = false;

    private Chunk readChunk = null;


    public ReadHandler(Transporter transporter, ChunkHandler chunkHandler) {
        this.transporter = transporter;
        this.chunkHandler = chunkHandler;
    }

    public void messageReceived(byte[] data) {
        LOGGER.trace("messageReceived");
        dispatch(data);
    }

    private void dispatch(byte[] data) {
        LOGGER.trace("dispatch");
        Message message = null;
        message = (Message) Objects.readObject(data);
        if (!greetDone) {
            greet(message);
        } else {
            readyToSendData();
        }


    }


    private void greet(Message message) {
        LOGGER.trace("greet:{}", message);
        Preconditions.checkArgument(message.getValue() != null, "the value of message should not null.");
        greetDone = true;
        transferDoing = true;
        transferDone = false;

        Chunk chunk = (Chunk) message.getValue();
        readChunk = chunk;
        LOGGER.trace("read chunk:{}", chunk);
        //check chunk extis
        if (chunkHandler.exists(chunk)) {
            transporter.writeBack(Message.createOkMessage());
        } else {
            transporter.writeBack(Message.createNoMessage());
        }
    }

    private void readyToSendData() {
        LOGGER.trace("sendData");
        InputStream inputStream = chunkHandler.readChunk(readChunk);
        transporter.switchDownToStreamMode();
        final long start = System.currentTimeMillis();
        ChannelFuture writeFuture = transporter.writeChunk(new ChunkedStream(inputStream));
        writeFuture.addListener(new ChannelFutureListener() {
            public void operationComplete(ChannelFuture channelFuture) throws Exception {
                long end = System.currentTimeMillis();
                LOGGER.info("complete:{}", (end - start));
                channelFuture.getChannel().close();
                transporter.close();
            }
        });
    }
}
