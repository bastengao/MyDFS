package com.gaohui.myDfs.storageServer;

import com.gaohui.myDfs.core.Chunk;
import com.gaohui.myDfs.core.StorageNode;
import com.gaohui.myDfs.leaderServer.service.storage.LeaderServerStorageService;
import com.gaohui.myDfs.storageServer.service.StorageServerService;
import com.gaohui.myDfs.storageServer.service.StorageServerServiceImpl;
import com.gaohui.myDfs.transfer.ChunkHandler;
import com.gaohui.myDfs.transfer.DataTransferServer;
import com.gaohui.rpc.client.DefaultClientServiceFactory;
import com.gaohui.rpc.client.DefaultServiceShop;
import com.gaohui.rpc.client.ServiceShop;
import com.gaohui.rpc.server.DefaultServiceBook;
import com.gaohui.rpc.server.ServiceBook;

import java.net.InetSocketAddress;
import java.util.Set;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * User: Administrator
 * Date: 10-12-29 Time: 下午12:31
 *
 * @author Basten Gao
 */
public class DataStorageServer {
    private static final String CHUNKS = "chunks";
    private LeaderServerStorageService leaderServerStorageService;
    private ServiceShop serviceShop;
    private ScheduledThreadPoolExecutor executor = new ScheduledThreadPoolExecutor(4);
    private StorageSystem storageSystem = null;
    private DataTransferServer dataTransferServer = null;
    private ServiceBook serviceBook = null;


    public DataStorageServer() {
        ChunkReaderWriter chunkWorker = new ChunkReaderWriter(CHUNKS); //TODO
        storageSystem = new StorageSystem(chunkWorker);
        ChunkHandler chunkHandlerAdapter = new ChunkHandlerAdapter(chunkWorker);
        dataTransferServer = new DataTransferServer(new InetSocketAddress(6666), chunkHandlerAdapter);

        serviceBook = new DefaultServiceBook(new InetSocketAddress(7777));
    }

    public void start() {
        serviceBook.register(StorageServerService.class, new StorageServerServiceImpl(storageSystem));

        serviceShop = new DefaultServiceShop(new DefaultClientServiceFactory(new InetSocketAddress(8888)));
        leaderServerStorageService = serviceShop.getService(LeaderServerStorageService.class);
        executor.scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
                Set<Chunk> chunks = storageSystem.getAllChunks();
                leaderServerStorageService.putChunkFileOfNode(new StorageNode("localhost", 8888), chunks);
            }
        }, 1, 10, TimeUnit.SECONDS);
    }

    public void close() {
        executor.shutdown();
        serviceShop.returnService(leaderServerStorageService);
    }
}
