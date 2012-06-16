package com.gaohui.myDfs.core;

import java.util.Set;

/**
 * User: Administrator
 * Date: 11-2-15 Time: 下午8:44
 *
 * @author Basten Gao
 */
public class ChunkImage {
    private Chunk chunk;
    private Set<StorageNode> storageNodes;

    public Chunk getChunk() {
        return chunk;
    }

    public void setChunk(Chunk chunk) {
        this.chunk = chunk;
    }

    public Set<StorageNode> getStorageNodes() {
        return storageNodes;
    }

    public void setStorageNodes(Set<StorageNode> storageNodes) {
        this.storageNodes = storageNodes;
    }

    @Override
    public String toString() {
        return "ChunkImage{" +
                "chunk=" + chunk +
                ", storageNodes=" + storageNodes +
                '}';
    }
}
