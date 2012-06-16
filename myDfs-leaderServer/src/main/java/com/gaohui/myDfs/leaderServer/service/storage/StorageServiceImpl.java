package com.gaohui.myDfs.leaderServer.service.storage;

import com.gaohui.myDfs.core.*;
import com.gaohui.myDfs.leaderServer.FileSystem;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

/**
 * User: Administrator
 * Date: 11-2-10 Time: 下午9:45
 *
 * @author Basten Gao
 */
public class StorageServiceImpl implements LeaderServerStorageService {
    private static Logger logger = LoggerFactory.getLogger(StorageServiceImpl.class);
    private FileSystem fileSystem = null;

    public StorageServiceImpl(FileSystem fileSystem) {
        this.fileSystem = fileSystem;
    }

    @Override
    public void putChunkFileOfNode(StorageNode node, Set<Chunk> chunks) {
        logger.debug("{} putChunks ", node);
        logger.trace("chunks {}", chunks);
        fileSystem.putChunkFileOfNode(node, chunks);
    }
}
