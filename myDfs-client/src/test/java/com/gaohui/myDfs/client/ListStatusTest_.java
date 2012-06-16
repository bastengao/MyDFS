package com.gaohui.myDfs.client;

import com.gaohui.myDfs.core.FileStatus;
import com.gaohui.myDfs.core.Path;

import java.net.InetSocketAddress;

/**
 * User: Administrator
 * Date: 11-4-28 Time: 下午10:12
 *
 * @author Basten Gao
 */
public class ListStatusTest_ {
    public static void main(String[] args) {
        FileSystem fileSystem = new FileSystemImpl(new InetSocketAddress(8888));
        FileStatus[] fileStatuses = fileSystem.listFileStatus(new Path("/a"));
        for (FileStatus fileStatus : fileStatuses) {
            System.out.println("fileStatus = " + fileStatus);
        }

        fileSystem.close();
    }
}
