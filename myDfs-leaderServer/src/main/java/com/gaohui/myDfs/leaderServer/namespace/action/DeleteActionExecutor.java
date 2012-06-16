package com.gaohui.myDfs.leaderServer.namespace.action;

import com.gaohui.myDfs.core.Paths;
import com.gaohui.myDfs.leaderServer.namespace.PathNode;
import com.gaohui.myDfs.leaderServer.namespace.RichPath;
import com.google.common.base.Preconditions;

import java.util.Collection;
import java.util.Iterator;
import java.util.Set;

/**
 * Immutable
 * User: Administrator
 * Date: 11-4-26 Time: 上午12:20
 *
 * @author Basten Gao
 */
public class DeleteActionExecutor implements ActionExecutor {
    @Override
    public boolean execute(Set<PathNode> rootNodes, NamespaceAction action) {
        RichPath[] paths = action.getTargetPaths();
        Preconditions.checkArgument(paths.length == 1);
        RichPath path = paths[0];

        String[] names = Paths.breaks(path.getPath().getPath());
        //如果是 "root"下面的节点
        if (names.length == 1) {
            if (removePathNode(path, rootNodes)) {
                return true;
            }
        } else {  //如果是 "root"下面的非直接孩子
            PathNode parentNode = PathNode.findParentPathNode(rootNodes, names);
            if (removePathNode(path, parentNode.getChildren())) {
                return true;
            }
        }

        return false;
    }

    private boolean removePathNode(RichPath path, Collection<PathNode> collection) {
        for (Iterator<PathNode> iterator = collection.iterator(); iterator.hasNext();) {
            PathNode node = iterator.next();
            if (node.getPath().equals(path.getPath())) {
                iterator.remove();
                return true;
            }
        }
        return false;
    }

}
