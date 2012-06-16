package com.gaohui.myDfs.leaderServer.service.storage;

import com.gaohui.myDfs.core.Chunk;
import com.gaohui.myDfs.core.StorageNode;

import java.util.List;
import java.util.Set;

/**
 * 面向StorageServer的服务
 * User: Administrator
 * Date: 11-2-3 Time: 下午9:56
 *
 * @author Basten Gao
 */
public interface LeaderServerStorageService {

    public void putChunkFileOfNode(StorageNode node, Set<Chunk> files);
}
