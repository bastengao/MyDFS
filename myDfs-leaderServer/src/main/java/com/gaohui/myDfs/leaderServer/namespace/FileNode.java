package com.gaohui.myDfs.leaderServer.namespace;

import java.util.Collections;
import java.util.Set;

/**
 * User: Administrator
 * Date: 11-2-4 Time: 下午4:11
 *
 * @author Basten Gao
 */
public class FileNode extends PathNode {

    @Override
    public boolean isAllowsChildren() {
        return false;
    }

    /**
     * 返回空的不可变集合
     *
     * @return
     */
    @Override
    public Set<PathNode> getChildren() {
        return Collections.emptySet();
    }

    /**
     * 此方法没有任何作用，同时会抛出UnspportedOperationException
     *
     * @param children
     */
    @Override
    public void setChildren(Set<PathNode> children) {
        throw new UnsupportedOperationException("此方法不支持");
    }
}
