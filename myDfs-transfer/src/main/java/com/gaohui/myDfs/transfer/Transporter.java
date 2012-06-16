package com.gaohui.myDfs.transfer;

import org.jboss.netty.channel.ChannelFuture;
import org.jboss.netty.handler.stream.ChunkedInput;

/**
 * User: Administrator
 * Date: 11-3-30 Time: 下午10:41
 *
 * @author Basten Gao
 */
public interface Transporter {

    void writeBack(Object value);

    void writeBack(byte[] data);

    void writeBack(byte[] buffer, int i, int length);

    void writeBackAwait(byte[] data);

    void writeBackAwait(byte[] buffer, int i, int length);

    void switchDownToStreamMode();

    void switchUpToStreamMode();

    ChannelFuture writeChunk(ChunkedInput chunkedInput);

    void close();
}
