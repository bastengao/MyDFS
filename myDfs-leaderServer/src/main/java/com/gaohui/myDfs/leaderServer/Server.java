package com.gaohui.myDfs.leaderServer;

/**
 * User: Administrator
 * Date: 11-5-1 Time: 上午10:02
 *
 * @author Basten Gao
 */
public class Server {
    public static void main(String[] args) {
        FileSystemServer fileSystemServer = new FileSystemServer();
        fileSystemServer.start();
    }
}
