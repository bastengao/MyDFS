package com.gaohui.myDfs.transfer;

import com.gaohui.myDfs.core.Chunk;

import java.io.*;
import java.net.InetSocketAddress;

/**
 * User: Administrator
 * Date: 11-3-4 Time: 上午11:45
 *
 * @author Basten Gao
 */
public class ServerTest_ {
    public static void main(String[] args) {

        ChunkHandler chunkHandler = new ChunkHandler() {
            @Override
            public boolean exists(Chunk chunk) {
                return true;
            }

            @Override
            public InputStream readChunk(Chunk chunk) {
                FileInputStream fileInputStream = null;
                try {
                    fileInputStream = new ByteToIntTest_.MyFileInputStream("E:\\a.txt");
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
                ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(new byte[]{1, 2, 3, 4, 5, 6, 7, 8, 9});
                return fileInputStream;
            }

            @Override
            public OutputStream writeChunk(Chunk chunk) {
                try {
                    return new FileOutputStream("e:/b.txt");
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
                return null;
            }
        };

        DataTransferServer server = new DataTransferServer(new InetSocketAddress(9999), chunkHandler);
    }
}
