package com.gaohui.myDfs.storageServer;

import com.gaohui.myDfs.core.Chunk;
import com.gaohui.myDfs.core.Path;

import java.io.File;
import java.io.FileNotFoundException;

/**
 * file format: [path]/[fileName].[ordinal].par e.m. /a/b/Cde.java.0.par
 * User: Administrator
 * Date: 11-2-3 Time: 下午11:19
 *
 * @author Basten Gao
 */
public class ChunkUtil {

    public static final String SEPARATOR = ".";
    public static final String POSTFIX = "par";

    /**
     * 根据父目录与当前文件名，解析出对应的chunk。不符合规范的文件名将返回null
     *
     * @param dirPath
     * @param fileName
     * @return
     */
    public static Chunk getChuck(String dirPath, String fileName) {
        String postfix = fileName.substring(fileName.length() - 3, fileName.length());
        if (!postfix.equals(POSTFIX)) {
            return null;
        }
        String fileNameWithoutPostfix = fileName.substring(0, fileName.length() - 4);
        int lastIndex2 = fileNameWithoutPostfix.lastIndexOf(SEPARATOR);

        String ordinalStr = fileNameWithoutPostfix.substring(lastIndex2 + 1);
        String trueFileName = fileNameWithoutPostfix.substring(0, lastIndex2);

        return new Chunk(new Path(dirPath + "/" + trueFileName), Integer.parseInt(ordinalStr));
    }

    /**
     * 根据Chunk解析出对应的File
     *
     * @param rootPath
     * @param chunk
     * @return
     * @throws FileNotFoundException 如果文件不存在
     * @throws IllegalStateException 如果文件是文件夹
     */
    public static File parseChunk(String rootPath, Chunk chunk) throws FileNotFoundException {
        File file = parseChunkWithoutCheck(rootPath, chunk);
        if (file == null) return null;
        if (!file.exists()) {
            throw new FileNotFoundException("chunk is not exists.");
        }
        if (file.isDirectory()) {
            throw new IllegalStateException("the file matching whit the chunk is not a file rather than a directory.");
        }
        return file;
    }

    /**
     * 根据Chunk解析出对应的File，但不对文件进行检查
     *
     * @param rootPath
     * @param chunk
     * @return
     */
    public static File parseChunkWithoutCheck(String rootPath, Chunk chunk) {
        if (chunk == null) {
            return null;
        }
        String chunkFileName = String.format("%s.%s.par", chunk.getOwner().getPath(), chunk.getOrdinal());
        File file = new File(rootPath, chunkFileName);
        return file;
    }
}
