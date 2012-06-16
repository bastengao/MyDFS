package com.gaohui.myDfs.leaderServer.namespace.action;

import com.gaohui.myDfs.leaderServer.namespace.PathNode;

import java.util.List;
import java.util.Set;

/**
 * Immutable
 * User: Administrator
 * Date: 11-4-26 Time: 上午12:18
 *
 * @author Basten Gao
 */
public interface ActionExecutor {
    public boolean execute(Set<PathNode> rootNodes, NamespaceAction action);
}
