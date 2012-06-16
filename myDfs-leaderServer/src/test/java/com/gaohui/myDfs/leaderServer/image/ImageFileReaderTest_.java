package com.gaohui.myDfs.leaderServer.image;

import com.gaohui.myDfs.leaderServer.namespace.DirectoryNode;
import com.gaohui.myDfs.leaderServer.namespace.FileNode;
import com.gaohui.myDfs.leaderServer.namespace.PathNode;
import com.gaohui.myDfs.leaderServer.namespace.image.ImageFileReader;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.thoughtworks.xstream.XStream;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.Arrays;

/**
 * User: Administrator
 * Date: 11-2-4 Time: 下午7:16
 *
 * @author Basten Gao
 */
public class ImageFileReaderTest_ {
    public static void main(String[] args) throws FileNotFoundException {
        PathNode pathNode = new DirectoryNode();
        PathNode a = new DirectoryNode();
        a.setName("a");

        PathNode b = new DirectoryNode();
        b.setName("b");

        PathNode c = new FileNode();
        c.setName("c.java");

        b.setChildren(Sets.newHashSet(c));

        a.setChildren(Sets.newHashSet(b));

        FileOutputStream outputStream = new FileOutputStream(ClassLoader.getSystemResource(ImageFileReader.IMAGE_FILE_PATH).getFile());

        XStream xStream = new XStream();
        xStream.toXML(new PathNode[]{a}, outputStream);

        System.out.println(c);
    }
}
