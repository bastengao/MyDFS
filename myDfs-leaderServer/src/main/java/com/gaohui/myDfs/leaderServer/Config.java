package com.gaohui.myDfs.leaderServer;

import com.google.common.base.Preconditions;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * User: Administrator
 * Date: 11-4-25 Time: 下午9:30
 *
 * @author Basten Gao
 */
public class Config {
    public static final String RPC_PORT = "server.rpc.port";
    private static final String CONFIG_PROPERTIES = "config.properties";
    private String propertiesPath = null;
    private Properties properties = new Properties();
    private int rpcPort = -1;

    /**
     * 创建Config,使用默认配置路径 "config.properties"
     *
     * @throws IOException 如果读取文件有异常
     */
    public Config() throws IOException {
        this(CONFIG_PROPERTIES);
    }

    /**
     * 创建Config
     *
     * @param propertiesPath 配置路径
     * @throws IOException 如果读取文件有异常
     */
    public Config(String propertiesPath) throws IOException {
        this(ClassLoader.getSystemResourceAsStream(propertiesPath));
    }

    /**
     * 创建Config
     *
     * @param inputStream 配置文件输入流
     * @throws IOException 如果读取文件有异常
     */
    public Config(InputStream inputStream) throws IOException {
        Preconditions.checkNotNull(inputStream, "inputStream should not be null");

        properties.load(inputStream);
        rpcPort = Integer.parseInt(properties.getProperty(RPC_PORT));
    }

    /**
     * 返回rpc对应的监听端口
     *
     * @return
     */
    public int getRpcPort() {
        return rpcPort;
    }
}
