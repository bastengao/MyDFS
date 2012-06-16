package com.gaohui.myDfs.leaderServer.namespace.image;

import com.gaohui.myDfs.leaderServer.namespace.PathNode;
import com.gaohui.myDfs.leaderServer.namespace.action.ActionExecutorFactory;
import com.gaohui.myDfs.leaderServer.namespace.action.NamespaceAction;
import com.thoughtworks.xstream.XStream;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.List;
import java.util.Set;

/**
 * User: Administrator
 * Date: 11-4-25 Time: 下午11:41
 *
 * @author Basten Gao
 */
public class ImageFileWriter {

    public void merge(Set<PathNode> rootNodes, List<NamespaceAction> actions, FileOutputStream fileOutputStream) {
        for (NamespaceAction action : actions) {
            ActionExecutorFactory.getActionExecution(action).execute(rootNodes, action);
        }
        XStream xStream = new XStream();
        xStream.toXML(rootNodes.toArray(new PathNode[0]), fileOutputStream);
    }


}
