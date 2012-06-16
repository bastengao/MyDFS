package com.gaohui.myDfs.leaderServer.namespace.action;

import com.gaohui.myDfs.core.Paths;
import com.gaohui.myDfs.leaderServer.namespace.DirectoryNode;
import com.gaohui.myDfs.leaderServer.namespace.FileNode;
import com.gaohui.myDfs.leaderServer.namespace.PathNode;
import com.gaohui.myDfs.leaderServer.namespace.RichPath;
import com.google.common.base.Preconditions;

import java.util.Set;

/**
 * Immutable
 * User: Administrator
 * Date: 11-4-26 Time: 上午12:19
 *
 * @author Basten Gao
 */
public class AddActionExecutor implements ActionExecutor {

    @Override
    public boolean execute(Set<PathNode> rootNodes, NamespaceAction action) {
        RichPath[] paths = action.getTargetPaths();
        Preconditions.checkArgument(paths.length == 1);

        RichPath path = paths[0];

        String[] names = Paths.breaks(path.getPath().getPath());


        PathNode node = null;
        if (path.isDirectory()) {
            node = new DirectoryNode();
        } else {
            node = new FileNode();
        }
        node.setName(names[names.length - 1]);

        //如果names的长度为1,那么path则属于root "/"
        if (names.length == 1) {
            return rootNodes.add(node);
        } else {
            PathNode parentNode = PathNode.findParentPathNode(rootNodes, names);
            if (!parentNode.isAllowsChildren()) {
                //TODO  check parentNode whether is direcotry or file
            }
            DirectoryNode directoryNode = (DirectoryNode) parentNode;
            node.setParent(directoryNode);
            return directoryNode.getChildren().add(node); //TODO check whether success or not
        }
    }

}
