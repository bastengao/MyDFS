package com.gaohui.myDfs.leaderServer.image;

import com.gaohui.myDfs.leaderServer.namespace.PathNode;
import com.gaohui.myDfs.leaderServer.namespace.image.ImageFileReader;
import org.junit.Test;

/**
 * User: Administrator
 * Date: 11-2-4 Time: 下午7:29
 *
 * @author Basten Gao
 */
public class ImageFileReaderTest {
    @Test
    public void test() {
        PathNode[] rootNode = ImageFileReader.getRootPathNode();
        System.out.println(rootNode);
    }
}
