package com.gaohui.myDfs.storageServer.service;

import com.gaohui.myDfs.core.Chunk;
import com.gaohui.myDfs.core.Path;

/**
 * User: Administrator
 * Date: 11-4-9 Time: 下午5:11
 *
 * @author Basten Gao
 */
public interface StorageServerService {

    /**
     * 删除文件
     *
     * @param chunk
     */
    public void deleteChunk(Chunk chunk);

    /**
     * 删除文件
     *
     * @param path
     */
    public void deleteFile(Path path);
}
