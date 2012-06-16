package com.gaohui.myDfs.client;

import com.gaohui.myDfs.core.FileStatus;
import com.gaohui.myDfs.core.Path;

import java.net.InetSocketAddress;

/**
 * User: Administrator
 * Date: 11-2-4 Time: 下午7:01
 *
 * @author Basten Gao
 */
public class FileSystemTest_ {
    public static void main(String[] args) {
        FileSystem fileSystem = new FileSystemImpl(new InetSocketAddress(8888));
        FileStatus fileStatus = fileSystem.getFileStatus(new Path("/a/b/d.java"));
        if (fileStatus != null) {
            System.out.println(fileStatus.getPath());
        }

        fileSystem.close();
    }
}
