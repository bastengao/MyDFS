package com.gaohui.myDfs.leaderServer.namespace;

import com.gaohui.myDfs.core.Path;
import com.gaohui.myDfs.core.Paths;
import com.google.common.base.Preconditions;

import java.util.Arrays;
import java.util.List;
import java.util.Set;

/**
 * 相对于 Path 的弱父子关系 ,PathNode 拥有强父子关系
 * User: Administrator
 * Date: 11-2-4 Time: 下午4:06
 *
 * @author Basten Gao
 */
public abstract class PathNode {
    private String name;
    protected PathNode parent;

    public PathNode getParent() {
        return parent;
    }

    public void setParent(PathNode parent) {
        this.parent = parent;

    }

    /**
     * @return 返回此PathNode对应的path
     */
    private String getPathString() {
        if (parent == null) {
            return Path.PATH_SEPARATOR + name;
        } else {
            return parent.getPathString() + Path.PATH_SEPARATOR + name;
        }
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return 返回此PathNode对应的Path
     */
    public Path getPath() {
        return new Path(getPathString());
    }

    /**
     * @return 是否允许有孩子
     */
    public abstract boolean isAllowsChildren();

    /**
     * @return 返回孩子
     */
    public abstract Set<PathNode> getChildren();

    /**
     * @param children 设置孩子
     */
    public abstract void setChildren(Set<PathNode> children);

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        PathNode pathNode = (PathNode) o;

        if (!name.equals(pathNode.name)) {
            return false;
        }
        if (parent != null ? !parent.equals(pathNode.parent) : pathNode.parent != null) {
            return false;
        }

        return true;
    }

    @Override
    public int hashCode() {
        int result = name.hashCode();
        result = 31 * result + (parent != null ? parent.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return getPathString();
    }

    public static List<String> getPathNodeNames(Path path) {
        String[] names = path.getPath().substring(1).split(Path.PATH_SEPARATOR);
        return Arrays.asList(names);
    }

    public static PathNode findPathNodeByPath(Set<PathNode> nodes, String path) {
        String[] names = Paths.breaks(path);
        return findPathNode(nodes, names);
    }

    public static PathNode findParentPathNode(Set<PathNode> nodes, String[] names) {
        return findPathNode(nodes, Arrays.copyOfRange(names, 0, names.length - 1));
    }


    public static PathNode findPathNode(Set<PathNode> nodes, String[] names) {
        Preconditions.checkArgument(names.length >= 1);
        String name = names[0];
        PathNode node = findPathNode(nodes, name);
        if (names.length == 1) {
            return node;
        }
        if (node.isAllowsChildren()) {
            return findPathNode(node.getChildren(), Arrays.copyOfRange(names, 1, names.length));
        }
        return null;
    }

    public static PathNode findPathNode(Set<PathNode> nodes, String name) {
        for (PathNode node : nodes) {
            if (node.getName().equals(name)) {
                return node;
            }
        }
        return null;
    }
}
