package com.kafeihu.task.listener;

import com.kafeihu.task.core.TaskExecution;

/**
 * date: 2022/7/25 16:10
 * writer listener
 *
 * @author kroulzhang
 */
public interface IWriterListener extends IListener {


    void beforeWrite(TaskExecution execution);

    void afterWrite(TaskExecution execution);

    void onWriteError(TaskExecution execution, Exception e);

    @Override
    default void beforeExecute(TaskExecution execution) {
        beforeWrite(execution);
    }

    @Override
    default void afterExecute(TaskExecution execution) {
        afterWrite(execution);
    }

    @Override
    default void onExecuteError(TaskExecution execution, Exception e) {
        onWriteError(execution, e);
    }

}
