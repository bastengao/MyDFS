package com.gaohui.myDfs.storageServer;

import com.gaohui.myDfs.core.Chunk;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

/**
 * User: Administrator
 * Date: 11-2-3 Time: 下午11:17
 *
 * @author Basten Gao
 */
public class Scanner {

    /**
     * 通过chunk的储存根目录，返回所有的Chunk
     *
     * @param baseDir
     * @return
     */
    public static Set<Chunk> getChunks(String baseDir) {
        File baseDirFile = new File(baseDir);
        File[] files = baseDirFile.listFiles();
        Set chunks = new HashSet<Chunk>();
        if (files != null) {
            for (File file : files) {
                addChunks("", file, chunks);
            }
        }
        return chunks;
    }

    private static void addChunks(String parentPath, File file, Set<Chunk> chunks) {
        if (file.isFile()) {
            Chunk chunk = ChunkUtil.getChuck(parentPath, file.getName());
            if (chunk != null) {
                chunks.add(chunk);
            }
        } else if (file.isDirectory()) {
            File[] files = file.listFiles();
            if (files != null) {
                for (File childFile : files) {
                    addChunks(parentPath + "/" + file.getName(), childFile, chunks);
                }
            }
        }
    }

    /**
     * 查看Chunk是否在本地文件系统中存在
     *
     * @param rootPath
     * @param chunk
     * @return
     */
    public static boolean exists(String rootPath, Chunk chunk) {
        try {
            File file = ChunkUtil.parseChunk(rootPath, chunk);
            if (file != null) {
                return true;
            }
        } catch (FileNotFoundException e) {
            return false;
        } catch (IllegalStateException e) {
            return false;
        }
        return false;
    }

    /**
     * 返回Chunk对应本地文件系统中的File.如果文件不存在返回null
     *
     * @param rootPath
     * @param chunk
     * @return
     */
    public static File getFile(String rootPath, Chunk chunk) {
        try {
            File file = ChunkUtil.parseChunk(rootPath, chunk);
            if (file != null) {
                return file;
            }
        } catch (FileNotFoundException e) {
            return null;
        } catch (IllegalStateException e) {
            return null;
        }
        return null;
    }

    /**
     * 解析Chun为对应File(文件不一定存在)
     *
     * @param rootPath
     * @param chunk
     * @return
     */
    public static File parseChunk(String rootPath, Chunk chunk) {
        return ChunkUtil.parseChunkWithoutCheck(rootPath, chunk);
    }
}
