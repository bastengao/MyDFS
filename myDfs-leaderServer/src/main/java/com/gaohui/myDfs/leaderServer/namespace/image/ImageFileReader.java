package com.gaohui.myDfs.leaderServer.namespace.image;

import com.gaohui.myDfs.leaderServer.namespace.PathNode;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.StreamException;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

/**
 * 整个文件系统的映像文件的Reader
 * User: Administrator
 * Date: 11-2-4 Time: 下午5:09
 *
 * @author Basten Gao
 */
public class ImageFileReader {
    public static final String IMAGE_FILE_PATH = "namespace/fileNamespace.img";

    /**
     * nothing
     */
    private ImageFileReader() {
    }

    /**
     * 返回文件系统中的根节点
     *
     * @return
     */
    public static PathNode[] getRootPathNode() {
        ensureFileExists();
        InputStream in = null; // ClassLoader.getSystemResourceAsStream(IMAGE_FILE_PATH);
        try {
            in = new FileInputStream(IMAGE_FILE_PATH);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        XStream xStream = new XStream();
        Object value;
        try {
            value = xStream.fromXML(in);
        } catch (StreamException ex) {
            return new PathNode[0];
        }
        return (PathNode[]) value;
    }

    private static void ensureFileExists() {
        File imageDirectory = new File("namespace");
        if (!imageDirectory.exists()) {
            imageDirectory.mkdir();
        }
        File imageFile = new File(IMAGE_FILE_PATH);
        if (!imageFile.exists()) {
            try {
                imageFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
