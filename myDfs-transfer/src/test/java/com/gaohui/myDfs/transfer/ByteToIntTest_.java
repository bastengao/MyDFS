package com.gaohui.myDfs.transfer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;

/**
 * User: Administrator
 * Date: 11-4-2 Time: 上午9:58
 *
 * @author Basten Gao
 */
public class ByteToIntTest_ {
    public static void main(String[] args) throws IOException {
        byte b = -1;
        int i = (b);
        System.out.println(i);

        i = 128;
        b = (byte) i;
        System.out.println(b);

        b = -2;
        i = b + 256;
        System.out.println(i);

//        InputStream inputStream = new MyFileInputStream("C:\\some\\暖春 DVD.rmvb");
//        byte[] buffer = new byte[2048];
//        for (int length = inputStream.read(buffer); length != -1; length = inputStream.read(buffer)) {
//        }
    }

    public static class MyFileInputStream extends FileInputStream {

        private static final Logger logger = LoggerFactory.getLogger(MyFileInputStream.class);

        public MyFileInputStream(String name) throws FileNotFoundException {
            super(name);
        }

        public MyFileInputStream(File file) throws FileNotFoundException {
            super(file);
        }

        public MyFileInputStream(FileDescriptor fdObj) {
            super(fdObj);
        }

        @Override
        public int read() throws IOException {
            int i = super.read();
            logger.trace("read:{}", i);
            return i;
        }

        @Override
        public int read(byte[] b) throws IOException {
            int length = super.read(b);
            logger.trace("readArray:{}", b);
            return length;
        }
    }

    public static class MyOutputStream extends OutputStream {
        @Override
        public void write(int b) throws IOException {

        }
    }
}
