package com.gaohui.myDfs.storageServer.service;

import com.gaohui.myDfs.core.Chunk;
import com.gaohui.myDfs.core.Path;
import com.gaohui.myDfs.core.StorageNode;
import com.gaohui.myDfs.storageServer.StorageSystem;

import java.util.Set;

/**
 * User: Administrator
 * Date: 11-4-16 Time: 下午4:59
 *
 * @author Basten Gao
 */
public class StorageServerServiceImpl implements StorageServerService {

    private StorageSystem storageSystem = null;

    public StorageServerServiceImpl(StorageSystem storageSystem) {
        this.storageSystem = storageSystem;
    }

    @Override
    public void deleteChunk(Chunk chunk) {

    }

    @Override
    public void deleteFile(Path path) {
        storageSystem.deleteFile(path);
    }
}
