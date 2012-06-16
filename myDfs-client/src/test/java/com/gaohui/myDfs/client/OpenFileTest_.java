package com.gaohui.myDfs.client;

import com.gaohui.myDfs.core.Path;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.InetSocketAddress;

/**
 * User: Administrator
 * Date: 11-2-27 Time: 下午9:40
 *
 * @author Basten Gao
 */
public class OpenFileTest_ {
    public static void main(String[] args) {
        FileSystemImpl fileSystem = new FileSystemImpl(new InetSocketAddress(8888));
        InputStreamReader reader = null;
        try {
            InputStream inputStream = fileSystem.openFile(new Path("/a/b/d.java"));
            reader = new InputStreamReader(inputStream);
            for (int i = reader.read(); i != -1; i = reader.read()) {
                System.out.print((char) i);
            }
            System.out.println();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();//TODO
                }
            }
        }
        fileSystem.close();
    }
}
