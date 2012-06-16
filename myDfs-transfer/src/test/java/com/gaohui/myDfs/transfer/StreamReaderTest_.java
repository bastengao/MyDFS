package com.gaohui.myDfs.transfer;

import com.gaohui.myDfs.core.Chunk;
import com.gaohui.myDfs.core.Path;
import com.google.common.io.ByteStreams;

import java.io.*;
import java.net.InetSocketAddress;

/**
 * User: Administrator
 * Date: 11-3-31 Time: 上午1:25
 *
 * @author Basten Gao
 */
public class StreamReaderTest_ {
    public static void main(String[] args) throws IOException {
        StreamReader reader = new NettyStreamReader();
        reader.connect(new InetSocketAddress(9999));
        Chunk chunk = new Chunk(new Path("/a/b/c.java"), 0);
        InputStream in = null;
        try {
            reader.greet(chunk);
            in = reader.openStream();
            FileOutputStream fileOutputStream = new FileOutputStream("e:\\c.txt");
            ByteStreams.copy(in, fileOutputStream);
            fileOutputStream.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            in.close();
            reader.close();
        }
    }
}
