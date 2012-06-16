package com.gaohui.myDfs.leaderServer.action;

import com.gaohui.myDfs.core.Path;
import com.gaohui.myDfs.leaderServer.namespace.PathNode;
import com.gaohui.myDfs.leaderServer.namespace.RichPath;
import com.gaohui.myDfs.leaderServer.namespace.action.ActionType;
import com.gaohui.myDfs.leaderServer.namespace.action.EditLogReader;
import com.gaohui.myDfs.leaderServer.namespace.action.EditLogWriter;
import com.gaohui.myDfs.leaderServer.namespace.action.NamespaceAction;
import com.gaohui.myDfs.leaderServer.namespace.image.ImageFileReader;
import com.gaohui.myDfs.leaderServer.namespace.image.ImageFileWriter;
import com.google.common.collect.Sets;
import org.junit.Assert;
import org.junit.Test;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

/**
 * User: Administrator
 * Date: 11-4-25 Time: 下午11:54
 *
 * @author Basten Gao
 */
public class NamespaceActionTest {
    @Test
    public void test() throws IOException, ClassNotFoundException {
        NamespaceAction action = new NamespaceAction(ActionType.ADD, new RichPath[]{new RichPath(new Path("/a/b/c.java"), false)});

        EditLogWriter writer = new EditLogWriter();
        writer.recordAction(action);
        writer.close();


        EditLogReader reader = new EditLogReader();
        List<NamespaceAction> actions = reader.getActions();
        Assert.assertEquals(1, actions.size());
        Assert.assertEquals(action, actions.get(0));
    }


    @Test
    public void test2() throws FileNotFoundException {
        NamespaceAction action = new NamespaceAction(ActionType.ADD, new RichPath[]{new RichPath(new Path("/a/b/d.java"), false)});
        PathNode[] rootNodes = ImageFileReader.getRootPathNode();

        Set<PathNode> roots = Sets.newHashSet(rootNodes);

        ImageFileWriter writer = new ImageFileWriter();
        writer.merge(roots, Arrays.asList(action), new FileOutputStream("E:/" + ImageFileReader.IMAGE_FILE_PATH));

        PathNode pathNode0 = PathNode.findPathNode(roots, "a");
        PathNode pathNode1 = PathNode.findPathNode(pathNode0.getChildren(), "b");
        PathNode pathNode2 = PathNode.findPathNode(pathNode1.getChildren(), "d.java");
        Assert.assertEquals("d.java", pathNode2.getName());
        Assert.assertEquals("/a/b/d.java", pathNode2.getPath().getPath());
    }

    @Test
    public void test3() throws FileNotFoundException {
        NamespaceAction action = new NamespaceAction(ActionType.DELETE, new RichPath[]{new RichPath(new Path("/a/b/c.java"), false)});
        PathNode[] rootNodes = ImageFileReader.getRootPathNode();

        Set<PathNode> roots = Sets.newHashSet(rootNodes);

        ImageFileWriter writer = new ImageFileWriter();
        writer.merge(roots, Arrays.asList(action), new FileOutputStream("E:/" + ImageFileReader.IMAGE_FILE_PATH));
        PathNode pathNode0 = PathNode.findPathNode(roots, "a");
        PathNode pathNode1 = PathNode.findPathNode(pathNode0.getChildren(), "b");
        PathNode pathNode2 = PathNode.findPathNode(pathNode1.getChildren(), "c.java");
        Assert.assertNull(pathNode2);
    }

    @Test
    public void test4() throws FileNotFoundException {
        NamespaceAction action = new NamespaceAction(ActionType.DELETE, new RichPath[]{new RichPath(new Path("/a/b"), false)});
        PathNode[] rootNodes = ImageFileReader.getRootPathNode();

        Set<PathNode> roots = Sets.newHashSet(rootNodes);

        ImageFileWriter writer = new ImageFileWriter();
        writer.merge(roots, Arrays.asList(action), new FileOutputStream("E:/" + ImageFileReader.IMAGE_FILE_PATH));
        PathNode pathNode0 = PathNode.findPathNode(roots, "a");
        PathNode pathNode1 = PathNode.findPathNode(pathNode0.getChildren(), "b");
        Assert.assertNull(pathNode1);
    }

    @Test
    public void test5() throws FileNotFoundException {
        NamespaceAction action = new NamespaceAction(ActionType.RENAME, new RichPath[]{new RichPath(new Path("/a/b/c.java"), false), new RichPath(new Path("/a/b/d.java"), false)});
        PathNode[] rootNodes = ImageFileReader.getRootPathNode();

        Set<PathNode> roots = Sets.newHashSet(rootNodes);

        ImageFileWriter writer = new ImageFileWriter();
        writer.merge(roots, Arrays.asList(action), new FileOutputStream("E:/" + ImageFileReader.IMAGE_FILE_PATH));

        PathNode pathNode0 = PathNode.findPathNode(roots, "a");
        PathNode pathNode1 = PathNode.findPathNode(pathNode0.getChildren(), "b");
        PathNode pathNode2 = PathNode.findPathNode(pathNode1.getChildren(), "c.java");
        Assert.assertNull(pathNode2);

        PathNode pathNode3 = PathNode.findPathNode(pathNode1.getChildren(), "d.java");
        Assert.assertEquals("d.java", pathNode3.getName());
        Assert.assertEquals("/a/b/d.java", pathNode3.getPath().getPath());
    }

    @Test
    public void test6() throws FileNotFoundException {
        NamespaceAction action = new NamespaceAction(ActionType.RENAME, new RichPath[]{new RichPath(new Path("/a/b"), false), new RichPath(new Path("/a/c"), false)});
        PathNode[] rootNodes = ImageFileReader.getRootPathNode();

        Set<PathNode> roots = Sets.newHashSet(rootNodes);

        ImageFileWriter writer = new ImageFileWriter();
        writer.merge(roots, Arrays.asList(action), new FileOutputStream("E:/" + ImageFileReader.IMAGE_FILE_PATH));

        PathNode pathNode0 = PathNode.findPathNode(roots, "a");
        PathNode pathNode1 = PathNode.findPathNode(pathNode0.getChildren(), "b");

        Assert.assertNull(pathNode1);

        PathNode pathNode2 = PathNode.findPathNode(pathNode0.getChildren(), "c");
        Assert.assertEquals("c", pathNode2.getName());
        Assert.assertEquals("/a/c", pathNode2.getPath().getPath());
    }
}
