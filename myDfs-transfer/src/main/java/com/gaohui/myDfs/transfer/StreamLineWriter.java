package com.gaohui.myDfs.transfer;

import com.gaohui.myDfs.core.Chunk;

import java.io.IOException;
import java.io.OutputStream;
import java.net.SocketAddress;
import java.util.List;

/**
 * example:
 * <pre>
 * StreamLineWriter streamLineWriter=...;
 * streamLineWriter.connectStreamLine(...);
 * streamLineWriter.greet();
 * streamLineWriter.send(bytes);
 * streamLineWriter.close();
 * </pre>
 * <p/>
 * User: Administrator
 * Date: 11-3-30 Time: 下午10:16
 *
 * @author Basten Gao
 */
public interface StreamLineWriter {
    void connectStreamLine(List<SocketAddress> nodes) throws IOException;

    void greet(Chunk chunk);

    void send(byte[] data);

    void send(OutputStream outputStream);

    void switchToStreamMode();

    OutputStream getWriteStream();

    void close();
}
