package com.gaohui.myDfs.storageServer;

import com.gaohui.myDfs.core.Chunk;
import com.gaohui.myDfs.transfer.ChunkHandler;
import com.google.common.base.Preconditions;

import java.io.InputStream;
import java.io.OutputStream;

/**
 * User: Administrator
 * Date: 11-4-3 Time: 上午10:31
 *
 * @author Basten Gao
 */
public class ChunkHandlerAdapter implements ChunkHandler {
    private ChunkReaderWriter chunkWorker = null;

    public ChunkHandlerAdapter(ChunkReaderWriter chunkWorker) {
        Preconditions.checkNotNull(chunkWorker, "chunkWorker should not be null.");
        this.chunkWorker = chunkWorker;
    }

    @Override
    public boolean exists(Chunk chunk) {
        return chunkWorker.exists(chunk);
    }


    @Override
    public InputStream readChunk(Chunk chunk) {
        return chunkWorker.read(chunk);
    }

    @Override
    public OutputStream writeChunk(Chunk chunk) {
        return chunkWorker.write(chunk);
    }

}
