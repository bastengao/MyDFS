package com.gaohui.myDfs.leaderServer;

import com.gaohui.myDfs.leaderServer.service.client.ClientService;
import com.gaohui.myDfs.leaderServer.service.client.ClientServiceImpl;
import com.gaohui.myDfs.leaderServer.service.storage.LeaderServerStorageService;
import com.gaohui.myDfs.leaderServer.service.storage.StorageServiceImpl;
import com.gaohui.rpc.server.DefaultServiceBook;
import com.gaohui.rpc.server.ServiceBook;

import java.io.IOException;
import java.net.InetSocketAddress;

/**
 * User: Administrator
 * Date: 10-12-29 Time: 下午12:27
 *
 * @author Basten Gao
 */
public class FileSystemServer {

    /**
     * leaderServer的程序的入口
     */
    public void start() {
        Config config;
        try {
            config = new Config();
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
        ServiceBook serviceBook = new DefaultServiceBook(new InetSocketAddress(config.getRpcPort()));

        FileSystem fileSystem = new FileSystem();
        serviceBook.register(ClientService.class, new ClientServiceImpl(fileSystem));
        serviceBook.register(LeaderServerStorageService.class, new StorageServiceImpl(fileSystem));
    }
}
