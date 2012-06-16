package com.gaohui.myDfs.client;

import com.gaohui.myDfs.core.Path;
import com.google.common.io.ByteStreams;

import java.io.*;
import java.net.InetSocketAddress;

/**
 * User: Administrator
 * Date: 11-4-3 Time: 下午9:35
 *
 * @author Basten Gao
 */
public class WriteFileTest_ {
    public static void main(String[] args) throws IOException {
        FileSystemImpl fileSystem = new FileSystemImpl(new InetSocketAddress(8888));
        Path path = new Path("/a/b/e.java");
        OutputStream outputStream = fileSystem.createFile(path);

        InputStream inputStream = new FileInputStream("e:/a.txt");

        ByteStreams.copy(inputStream, outputStream);


        inputStream.close();
        outputStream.close();
        fileSystem.close();
    }
}
