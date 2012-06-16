package com.gaohui.myDfs.transfer;

import com.gaohui.myDfs.core.Chunk;

import java.io.InputStream;
import java.net.SocketAddress;

/**
 * User: Administrator
 * Date: 11-3-31 Time: 上午12:47
 *
 * @author Basten Gao
 */
public interface StreamReader {
    void connect(SocketAddress socketAddress);

    void greet(Chunk chunk);

    InputStream openStream();

    void close();
}
