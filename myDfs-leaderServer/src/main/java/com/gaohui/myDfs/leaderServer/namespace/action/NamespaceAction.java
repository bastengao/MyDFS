package com.gaohui.myDfs.leaderServer.namespace.action;

import com.gaohui.myDfs.leaderServer.namespace.RichPath;

import java.io.Serializable;
import java.util.Arrays;

/**
 * 表示名称空间操作
 * Immutable
 * User: Administrator
 * Date: 11-4-25 Time: 下午10:02
 *
 * @author Basten Gao
 */
public class NamespaceAction implements Serializable {
    private long serialVersionUID = 1L;

    private ActionType actionType;
    private RichPath[] targetPaths;

    /**
     * @param actionType  操作类型
     * @param targetPaths 操作作用目标
     */
    public NamespaceAction(ActionType actionType, RichPath[] targetPaths) {
        this.actionType = actionType;
        this.targetPaths = targetPaths;
    }

    public ActionType getActionType() {
        return actionType;
    }

    public RichPath[] getTargetPaths() {
        return targetPaths;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        NamespaceAction that = (NamespaceAction) o;

        if (actionType != that.actionType) return false;
        if (!Arrays.equals(targetPaths, that.targetPaths)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = actionType.hashCode();
        result = 31 * result + Arrays.hashCode(targetPaths);
        return result;
    }
}
