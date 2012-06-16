package com.gaohui.myDfs.client;

import com.gaohui.myDfs.core.FileStatus;
import com.gaohui.myDfs.core.Path;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * 客户端文件系统的接口
 * User: Administrator
 * Date: 11-4-25 Time: 下午9:16
 *
 * @author Basten Gao
 */
public interface FileSystem {
    /**
     * 获取FileStatus,如果不存在返回null
     *
     * @param path
     * @return 此路径对应的FileStatus
     */
    FileStatus getFileStatus(Path path);

    /**
     * 返回某个路径下的文件和文件夹
     *
     * @param path
     * @return
     */
    FileStatus[] listFileStatus(Path path);

    /**
     * 创建一个文件，并返回此文件的输出流
     *
     * @param path
     * @return
     */
    OutputStream createFile(Path path);

    /**
     * 打开一个文件
     *
     * @param path
     * @return
     * @throws FileNotFoundException 如果文件不存在
     */
    InputStream openFile(Path path) throws FileNotFoundException;

    /**
     * 删除一个文件或者路径
     *
     * @param path
     * @return
     */
    boolean delete(Path path);

    /**
     * 创建一个目录
     *
     * @param path
     * @return
     */
    boolean mkDir(Path path);

    /**
     * 重命名一个文件或者目录
     *
     * @param srcPath
     * @param dstPath
     * @return
     */
    boolean rename(Path srcPath, Path dstPath);

    /**
     * 关闭
     */
    void close();
}
