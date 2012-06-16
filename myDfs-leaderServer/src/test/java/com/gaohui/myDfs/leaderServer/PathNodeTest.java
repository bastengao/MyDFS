package com.gaohui.myDfs.leaderServer;

import com.gaohui.myDfs.core.Path;
import com.gaohui.myDfs.leaderServer.namespace.PathNode;
import org.junit.Assert;
import org.junit.Test;

import java.util.List;

/**
 * User: Administrator
 * Date: 11-2-4 Time: 下午8:28
 *
 * @author Basten Gao
 */
public class PathNodeTest {
    @Test
    public void test() {
        Path path = new Path("/a/b/c.java");
        List<String> names = PathNode.getPathNodeNames(path);
        Assert.assertEquals(names.get(0), "a");
        Assert.assertEquals(names.get(1), "b");
        Assert.assertEquals(names.get(2), "c.java");
    }
}
