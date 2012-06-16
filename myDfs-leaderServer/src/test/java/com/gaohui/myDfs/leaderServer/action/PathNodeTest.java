package com.gaohui.myDfs.leaderServer.action;

import com.gaohui.myDfs.core.Paths;
import com.gaohui.myDfs.leaderServer.namespace.PathNode;
import com.gaohui.myDfs.leaderServer.namespace.image.ImageFileReader;
import com.google.common.collect.Sets;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.Set;

/**
 * User: Administrator
 * Date: 11-4-26 Time: 下午10:50
 *
 * @author Basten Gao
 */
public class PathNodeTest {
    @Test
    public void test() {
        PathNode[] rootNodes = ImageFileReader.getRootPathNode();

        String path = "/a/b/c.java";

        PathNode pathNode = PathNode.findParentPathNode(Sets.newHashSet(rootNodes), Paths.breaks(path));
        Assert.assertEquals("/a/b", pathNode.getPath().getPath());
    }
}
