package com.gaohui.myDfs.leaderServer.service.client;

import com.gaohui.myDfs.core.FileImage;
import com.gaohui.myDfs.core.FileStatus;
import com.gaohui.myDfs.core.Path;
import com.gaohui.myDfs.core.StorageNode;

import java.util.Set;

/**
 * 面向客户端的服务
 * User: Administrator
 * Date: 11-2-3 Time: 下午11:13
 *
 * @author Basten Gao
 */
public interface ClientService {

    /**
     * @param path path
     * @return 获取某个路径的状态
     */
    FileStatus getFileStatus(Path path);

    /**
     * @param path
     * @return
     */
    FileStatus[] listFileStatus(Path path);

    /**
     * @param path path
     * @return 获取某个路径对应文件的状态
     */
    FileImage getFileImage(Path path);

    /**
     * @return 返回可用的StorageNode
     */
    Set<StorageNode> getAvailableStorageNode();

    /**
     * 创建一个文件
     *
     * @param path 文件对应的路径
     * @return 创建成功与否
     */
    boolean newFile(Path path);

    /**
     * 创建一个目录
     *
     * @param path path
     * @return 创建成功与否
     */
    boolean mkDir(Path path);

    /**
     * 将srcPath重命名为dstPath
     *
     * @param srcPath path
     * @param dstPath path
     * @return 成功与否
     */
    boolean rename(Path srcPath, Path dstPath);

    /**
     * 删除某个路径对应的文件或者目录
     *
     * @param path path
     * @return 成功与否
     */
    boolean delete(Path path);
}
