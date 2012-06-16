package com.gaohui.myDfs.transfer;

import com.gaohui.myDfs.core.Chunk;
import com.gaohui.myDfs.core.Path;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.util.Arrays;

/**
 * User: Administrator
 * Date: 11-3-4 Time: 上午11:35
 *
 * @author Basten Gao
 */
public class StreamWriterTest_ {
    public static void main(String[] args) throws IOException {
        StreamLineWriter writer = new NettyStreamLineWriter();
        writer.connectStreamLine(Arrays.<SocketAddress>asList( new InetSocketAddress(9999)));

        Chunk chunk = new Chunk(new Path("/a/b/c.java"), 0);
        writer.greet(chunk);
        System.out.println("after greet");
        String s = "hello,world.";

        OutputStream outputStream = writer.getWriteStream();
        for (int i = 0; i < 10; i++) {
            outputStream.write((s + i).getBytes());
        }

        outputStream.close();

        writer.close();
    }
}
