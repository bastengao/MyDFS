package com.gaohui.myDfs.storageServer;

import com.gaohui.myDfs.core.Chunk;
import com.gaohui.myDfs.core.Path;
import com.gaohui.myDfs.core.Paths;
import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

/**
 * User: Administrator
 * Date: 11-4-3 Time: 上午10:08
 *
 * @author Basten Gao
 */
public class ChunkReaderWriter {
    private static final Logger LOGGER = LoggerFactory.getLogger(ChunkReaderWriter.class);

    //chunk 的存储根目录
    private String baseDir;

    public ChunkReaderWriter(String baseDir) {
        this.baseDir = baseDir;
        ensureDirectoryExists(baseDir);
    }

    private void ensureDirectoryExists(String directoryName) {
        File directory = new File(directoryName);
        if (!directory.exists()) {
            directory.mkdir();
        }
    }

    /**
     * 返回所有的Chunk
     *
     * @return
     */
    public Set<Chunk> getAllChunks() {
        LOGGER.trace("getAllChunks");
        return Scanner.getChunks(baseDir);
    }

    public boolean exists(Chunk chunk) {
        LOGGER.trace("exists:{}", chunk);
        return Scanner.exists(baseDir, chunk);
    }

    /**
     * 获取某个chunk的输入流
     *
     * @param chunk
     * @return
     */
    public InputStream read(Chunk chunk) {
        LOGGER.trace("read:{}", chunk);
        File chunkFile = Scanner.getFile(baseDir, chunk);
        if (chunkFile == null) {
            //TODO
        }
        FileInputStream fileInputStream = null;
        try {
            fileInputStream = new FileInputStream(chunkFile);
        } catch (FileNotFoundException e) {
            e.printStackTrace(); //TODO
        }
        return fileInputStream;
    }

    /**
     * 获取某个chunk的输出流
     *
     * @param chunk
     * @return
     */
    public OutputStream write(Chunk chunk) {
        LOGGER.trace("write:{}", chunk);
        String[] names = Paths.breaks(chunk.getOwner().getPath());
        List<String> directoryNames = Lists.newArrayList(Arrays.copyOfRange(names, 0, names.length - 1));
        ensureDirectoryExists(baseDir, directoryNames);
        File chunkFile = Scanner.parseChunk(baseDir, chunk);
        Preconditions.checkArgument(!chunkFile.exists(), "chunk exists.");

        FileOutputStream fileOutputStream = null;
        try {
            fileOutputStream = new FileOutputStream(chunkFile);
        } catch (FileNotFoundException e) {
            e.printStackTrace(); //TODO
        }
        return fileOutputStream;
    }

    private void ensureDirectoryExists(String baseDir, List<String> directoryNames) {
        String fullPath = baseDir;
        for (String directory : directoryNames) {
            fullPath += "/" + directory;
            ensureDirectoryExists(fullPath);
        }
    }

    /**
     * 删除块
     *
     * @param path
     */
    public void deleteFile(Path path) {
        LOGGER.trace("delete:{}", path);
        Chunk chunk = new Chunk(path, 0); //TODO all file use one chunk
        File chunkFile = Scanner.parseChunk(baseDir, chunk);
        if (chunkFile != null) {
            if (chunkFile.exists()) {
                chunkFile.delete();
            }
        }
    }

}
