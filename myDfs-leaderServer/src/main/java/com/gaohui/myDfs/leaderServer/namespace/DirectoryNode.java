package com.gaohui.myDfs.leaderServer.namespace;

import com.gaohui.myDfs.core.Path;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * User: Administrator
 * Date: 11-2-4 Time: 下午4:11
 *
 * @author Basten Gao
 */
public class DirectoryNode extends PathNode {
    private Set<PathNode> children = new HashSet<PathNode>();
    private Map<Path, PathNode> pathPathNodeMap = new HashMap<Path, PathNode>();

    @Override
    public boolean isAllowsChildren() {
        return true;
    }

    @Override
    public void setChildren(Set<PathNode> children) {
        this.children = children;
        for (PathNode child : children) {
            child.setParent(this);
        }
    }

    @Override
    public Set<PathNode> getChildren() {
        return children;
    }
}
