package com.gaohui.myDfs.storageServer;

import com.gaohui.myDfs.core.Chunk;
import com.gaohui.myDfs.core.Path;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.Set;

/**
 * User: Administrator
 * Date: 11-4-1 Time: 下午11:42
 *
 * @author Basten Gao
 */
public class StorageSystem {
    private static final String CHUNKS = "chunks";
    private ChunkReaderWriter chunkWorker = null;

    public StorageSystem(ChunkReaderWriter chunkWorker) {
        this.chunkWorker = chunkWorker;
    }

    /**
     * 返回所有chunk
     *
     * @return
     */
    public Set<Chunk> getAllChunks() {
        return chunkWorker.getAllChunks();
    }

    /**
     * 查看对应的Chunk是否存在
     *
     * @param path
     * @param ordinal
     * @return
     */
    public boolean exists(Path path, int ordinal) {
        return chunkWorker.exists(new Chunk(path, ordinal));
    }

    /**
     * 查看对应的Chunk是否存在
     *
     * @param chunk
     * @return
     */
    public boolean exists(Chunk chunk) {
        return chunkWorker.exists(chunk);
    }

    /**
     * 读取Chunk
     *
     * @param chunk
     * @return
     */
    public InputStream readChunk(Chunk chunk) {
        return chunkWorker.read(chunk);
    }

    /**
     * 写Chunk
     *
     * @param chunk
     * @return
     */
    public OutputStream writeChunk(Chunk chunk) {
        return chunkWorker.write(chunk);
    }

    public void deleteFile(Path path) {
        //TODO
        chunkWorker.deleteFile(path);
    }
}
