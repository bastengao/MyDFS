package com.gaohui.myDfs.leaderServer.service.client;

import com.gaohui.myDfs.core.FileImage;
import com.gaohui.myDfs.core.FileStatus;
import com.gaohui.myDfs.core.Path;
import com.gaohui.myDfs.core.StorageNode;
import com.gaohui.myDfs.leaderServer.FileSystem;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Set;

/**
 * User: Administrator
 * Date: 11-2-4 Time: 下午6:37
 *
 * @author Basten Gao
 */
public class ClientServiceImpl implements ClientService {
    private static final Logger LOGGER = LoggerFactory.getLogger(ClientServiceImpl.class);

    private FileSystem fileSystem = null;

    /**
     * @param fileSystem 实现
     */
    public ClientServiceImpl(FileSystem fileSystem) {
        this.fileSystem = fileSystem;
    }

    @Override
    public FileStatus getFileStatus(Path path) {
        LOGGER.trace("getFileStatus:{}", path);
        return fileSystem.getFileStatus(path);
    }

    @Override
    public FileStatus[] listFileStatus(Path path) {
        LOGGER.trace("listfileStatus:{}", path);
        return fileSystem.listFileStatus(path);
    }

    @Override
    public FileImage getFileImage(Path path) {
        return fileSystem.getFileImage(path);
    }

    @Override
    public Set<StorageNode> getAvailableStorageNode() {
        return fileSystem.getStorageNodes();
    }

    @Override
    public boolean newFile(Path path) {
        LOGGER.trace("newFile:{}", path);
        return fileSystem.newFile(path);
    }

    @Override
    public boolean mkDir(Path path) {
        LOGGER.trace("mkDir:{}", path);
        return fileSystem.mkDir(path);
    }

    @Override
    public boolean rename(Path srcPath, Path dstPath) {
        LOGGER.trace("rename:src:{},dst:{}", srcPath, dstPath);
        return fileSystem.rename(srcPath, dstPath);
    }

    @Override
    public boolean delete(Path path) {
        LOGGER.trace("delete:{}", path);
        return fileSystem.delete(path);
    }


}
