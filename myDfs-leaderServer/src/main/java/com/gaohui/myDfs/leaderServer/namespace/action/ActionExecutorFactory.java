package com.gaohui.myDfs.leaderServer.namespace.action;

/**
 * Immutable
 * User: Administrator
 * Date: 11-4-26 Time: 上午12:18
 *
 * @author Basten Gao
 */
public class ActionExecutorFactory {
    private static final AddActionExecutor ADD_ACTION_EXECUTION = new AddActionExecutor();
    private static final DeleteActionExecutor DELETE_ACTION_EXECUTION = new DeleteActionExecutor();
    private static final RenameActionExecutor RENAME_ACTION_EXECUTION = new RenameActionExecutor();


    private ActionExecutorFactory() {
    }

    public static ActionExecutor getActionExecution(NamespaceAction action) {
        if (action.getActionType() == ActionType.ADD) {
            return ADD_ACTION_EXECUTION;
        } else if (action.getActionType() == ActionType.DELETE) {
            return DELETE_ACTION_EXECUTION;
        } else if (action.getActionType() == ActionType.RENAME) {
            return RENAME_ACTION_EXECUTION;
        }
        return null;
    }
}
