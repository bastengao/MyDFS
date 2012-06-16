package com.gaohui.myDfs.leaderServer.namespace.action;

import com.gaohui.myDfs.core.Paths;
import com.gaohui.myDfs.leaderServer.namespace.PathNode;
import com.gaohui.myDfs.leaderServer.namespace.RichPath;
import com.google.common.base.Preconditions;

import java.util.Set;

/**
 * Immutable
 * User: Administrator
 * Date: 11-4-26 Time: 上午12:20
 *
 * @author Basten Gao
 */
public class RenameActionExecutor implements ActionExecutor {
    @Override
    public boolean execute(Set<PathNode> rootNodes, NamespaceAction action) {
        RichPath[] paths = action.getTargetPaths();
        Preconditions.checkArgument(paths.length == 2);
        RichPath srcPath = paths[0];
        RichPath dstPath = paths[1];

        PathNode node = PathNode.findPathNode(rootNodes, Paths.breaks(srcPath.getPath().getPath()));

        if (node == null) {
            return false;
        }
        String[] names = Paths.breaks(dstPath.getPath().getPath());
        node.setName(names[names.length - 1]);
        return true;
    }
}
