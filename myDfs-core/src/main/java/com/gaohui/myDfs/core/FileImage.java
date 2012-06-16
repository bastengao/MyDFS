package com.gaohui.myDfs.core;

import java.util.HashMap;
import java.util.Map;

/**
 * User: Administrator
 * Date: 11-2-25 Time: 下午5:40
 *
 * @author Basten Gao
 */
public class FileImage {
    private Map<Integer, ChunkImage> chunkImageMap = new HashMap<Integer, ChunkImage>();

    public ChunkImage getChunkImage(int ordinal) {
        return chunkImageMap.get(ordinal);
    }

    public ChunkImage putChunkImage(int ordinal, ChunkImage chunkImage) {
        return chunkImageMap.put(ordinal, chunkImage);
    }

    @Override
    public String toString() {
        return "FileImage{" +
                "chunkImageMap=" + chunkImageMap +
                '}';
    }
}
