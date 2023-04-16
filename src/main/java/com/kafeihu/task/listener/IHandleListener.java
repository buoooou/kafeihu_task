package com.kafeihu.task.listener;

import com.kafeihu.task.core.TaskExecution;

/**
 * date: 2022/7/25 16:10
 * handle listener
 *
 * @author kroulzhang
 */
public interface IHandleListener extends IListener {

    void beforeHandle(TaskExecution execution);


    void afterHandle(TaskExecution execution);


    void onHandleError(TaskExecution execution, Exception e);

    @Override
    default void beforeExecute(TaskExecution execution) {
        beforeHandle(execution);
    }

    @Override
    default void afterExecute(TaskExecution execution) {
        afterHandle(execution);
    }

    @Override
    default void onExecuteError(TaskExecution execution, Exception e) {
        onHandleError(execution, e);
    }
}
