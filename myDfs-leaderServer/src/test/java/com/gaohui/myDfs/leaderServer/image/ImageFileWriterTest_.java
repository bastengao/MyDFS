package com.gaohui.myDfs.leaderServer.image;

import com.gaohui.myDfs.core.Path;
import com.gaohui.myDfs.leaderServer.namespace.PathNode;
import com.gaohui.myDfs.leaderServer.namespace.RichPath;
import com.gaohui.myDfs.leaderServer.namespace.action.ActionType;
import com.gaohui.myDfs.leaderServer.namespace.action.NamespaceAction;
import com.gaohui.myDfs.leaderServer.namespace.image.ImageFileReader;
import com.gaohui.myDfs.leaderServer.namespace.image.ImageFileWriter;
import com.google.common.collect.Sets;
import org.junit.Test;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.Arrays;
import java.util.Set;

/**
 * User: Administrator
 * Date: 11-4-27 Time: 上午12:11
 *
 * @author Basten Gao
 */
public class ImageFileWriterTest_ {


    public static void test() throws FileNotFoundException {
        NamespaceAction action = new NamespaceAction(ActionType.ADD, new RichPath[]{new RichPath(new Path("/a/b/d.java"), false)});
        PathNode[] rootNodes = ImageFileReader.getRootPathNode();

        Set<PathNode> roots = Sets.newHashSet(rootNodes);

        ImageFileWriter writer = new ImageFileWriter();
        writer.merge(roots, Arrays.asList(action), new FileOutputStream(ClassLoader.getSystemResource(ImageFileReader.IMAGE_FILE_PATH).getFile()));
    }
}
