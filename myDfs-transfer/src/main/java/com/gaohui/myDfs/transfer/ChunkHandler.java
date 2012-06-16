package com.gaohui.myDfs.transfer;

import com.gaohui.myDfs.core.Chunk;

import java.io.InputStream;
import java.io.OutputStream;

/**
 * User: Administrator
 * Date: 11-4-2 Time: 上午12:04
 *
 * @author Basten Gao
 */
public interface ChunkHandler {

    boolean exists(Chunk chunk);

    InputStream readChunk(Chunk chunk);

    OutputStream writeChunk(Chunk chunk);

}
