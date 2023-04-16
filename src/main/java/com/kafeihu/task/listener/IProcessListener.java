package com.kafeihu.task.listener;

import com.kafeihu.task.core.TaskExecution;


/**
 * date: 2022/7/25 16:10
 * process listener
 *
 * @author kroulzhang
 */
public interface IProcessListener extends IListener {

    void beforeProcess(TaskExecution execution);


    void afterProcess(TaskExecution execution);


    void onProcessError(TaskExecution execution, Exception e);

    @Override
    default void beforeExecute(TaskExecution execution) {
        beforeProcess(execution);
    }

    @Override
    default void afterExecute(TaskExecution execution) {
        afterProcess(execution);
    }

    @Override
    default void onExecuteError(TaskExecution execution, Exception e) {
        onProcessError(execution, e);
    }
}
