package com.gaohui.myDfs.client;

import com.gaohui.myDfs.core.*;
import com.gaohui.myDfs.leaderServer.service.client.ClientService;
import com.gaohui.myDfs.transfer.NettyStreamLineWriter;
import com.gaohui.myDfs.transfer.NettyStreamReader;
import com.gaohui.myDfs.transfer.StreamLineWriter;
import com.gaohui.myDfs.transfer.StreamReader;
import com.gaohui.rpc.client.DefaultClientServiceFactory;
import com.gaohui.rpc.client.DefaultServiceShop;
import com.gaohui.rpc.client.ServiceShop;
import com.google.inject.internal.Preconditions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * 客户端文件系统的接口
 * User: Administrator
 * Date: 10-12-29 Time: 下午12:22
 *
 * @author Basten Gao
 */
public class FileSystemImpl implements FileSystem {
    private static Logger logger = LoggerFactory.getLogger(FileSystemImpl.class);
    private ClientService clientService = null;
    private ServiceShop serviceShop = null;

    /**
     * @param address
     */
    public FileSystemImpl(SocketAddress address) {
        serviceShop = new DefaultServiceShop(new DefaultClientServiceFactory(address));
        clientService = serviceShop.getService(ClientService.class);
    }

    /**
     * 获取FileStatus,如果不存在返回null
     *
     * @param path
     * @return
     */
    @Override
    public FileStatus getFileStatus(Path path) {
        return clientService.getFileStatus(path);
    }

    @Override
    public FileStatus[] listFileStatus(Path path) {
        return clientService.listFileStatus(path);
    }

    /**
     * 创建文件，对返回此文件的输出流
     *
     * @param path
     * @return
     * @throws IllegalStateException 如果文件已经存在
     */
    @Override
    public OutputStream createFile(Path path) {
        FileStatus fileStatus = clientService.getFileStatus(path);
        if (fileStatus != null) {
            throw new IllegalStateException("file already exists");
        }
        clientService.newFile(path);
        Chunk chunk = new Chunk(path, 0);
        // get availabale storageNode
        Set<StorageNode> storageNodeSet = clientService.getAvailableStorageNode();
        List<SocketAddress> socketAddresses = new ArrayList<SocketAddress>();
        for (StorageNode storageNode : storageNodeSet) {
            SocketAddress socketAddress = new InetSocketAddress(storageNode.getLocation(), 6666);
            socketAddresses.add(socketAddress);
        }
        StreamLineWriter streamWriter = new NettyStreamLineWriter();
        try {
            streamWriter.connectStreamLine(socketAddresses);
        } catch (IOException e) {
            e.printStackTrace(); //TODO
        }
        streamWriter.greet(chunk);
        return streamWriter.getWriteStream();
    }

    @Override
    public InputStream openFile(Path path) throws FileNotFoundException {
        FileStatus fileStatus = clientService.getFileStatus(path);
        if (fileStatus == null) {
            throw new FileNotFoundException(path.toString());
        }
        //1.先看Path是否是文件，而不是文件夹
        Preconditions.checkArgument(fileStatus.isFile(), "path :" + path + " is not file.");

        //2.向leaderServer获取存储此Path相关Chunk的StorageNode
        FileImage fileImage = clientService.getFileImage(path);
        logger.debug("getted fileImage:{}", fileImage);
        //获取文件的块的个数

        StreamReader reader = new NettyStreamReader();
        List<StorageNode> storageNodes = new ArrayList<StorageNode>(fileImage.getChunkImage(0).getStorageNodes());
        StorageNode storageNode = storageNodes.get(0);
        reader.connect(new InetSocketAddress(storageNode.getLocation(), 6666)); //TODO replace port to correct port
        reader.greet(fileImage.getChunkImage(0).getChunk());
        //TODO
        //3.向其中的storageServer查看Chunk在否;如果不在,询问其他的备份storageServer
        //4.找到可用的Chunk后，在storageServer之间建立流传输数据
        return reader.openStream();
    }

    /**
     * 删除文件
     *
     * @param path
     */
    @Override
    public boolean delete(Path path) {
        return clientService.delete(path);
    }

    @Override
    public boolean mkDir(Path path) {
        return clientService.mkDir(path);
    }


    @Override
    public boolean rename(Path srcPath, Path dstPath) {
        return clientService.rename(srcPath, dstPath);
    }

    @Override
    public void close() {
        serviceShop.returnService(clientService);
    }
}
