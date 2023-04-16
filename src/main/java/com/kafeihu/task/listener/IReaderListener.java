package com.kafeihu.task.listener;

import com.kafeihu.task.core.TaskExecution;

/**
 * date: 2022/7/25 16:10
 * reader listener
 *
 * @author kroulzhang
 */
public interface IReaderListener extends IListener {

    void beforeReader(TaskExecution execution);


    void afterReader(TaskExecution execution);


    void onReaderError(TaskExecution execution, Exception e);


    @Override
    default void beforeExecute(TaskExecution execution) {
        beforeReader(execution);
    }

    @Override
    default void afterExecute(TaskExecution execution) {
        afterReader(execution);
    }

    @Override
    default void onExecuteError(TaskExecution execution, Exception e) {
        onReaderError(execution, e);
    }
}
