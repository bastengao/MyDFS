package com.gaohui.myDfs.leaderServer.namespace;

import com.gaohui.myDfs.leaderServer.namespace.action.EditLogReader;
import com.gaohui.myDfs.leaderServer.namespace.action.NamespaceAction;
import com.gaohui.myDfs.leaderServer.namespace.image.ImageFileReader;
import com.gaohui.myDfs.leaderServer.namespace.image.ImageFileWriter;
import com.google.common.collect.Sets;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Set;

/**
 * User: Administrator
 * Date: 11-4-27 Time: 下午11:40
 *
 * @author Basten Gao
 */
public class Namespaces {

    /**
     * util class
     */
    private Namespaces() {
    }

    /**
     * 将操作日志作用于当前的名称空间image，并将其覆盖新的image,并返回融合后的名称空间
     *
     * @return
     */
    public static Set<PathNode> processNamespace() {
        PathNode[] rootNodes = ImageFileReader.getRootPathNode();
        Set<PathNode> roots = Sets.newHashSet(rootNodes);


        EditLogReader logReader = null;
        try {
            logReader = new EditLogReader();
            List<NamespaceAction> actions = logReader.getActions();
            ImageFileWriter imageFileWriter = new ImageFileWriter();
            imageFileWriter.merge(roots, actions, new FileOutputStream(new File(ImageFileReader.IMAGE_FILE_PATH)));
        } catch (IOException e) {
            throw new IllegalStateException(e);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return roots;
    }
}
