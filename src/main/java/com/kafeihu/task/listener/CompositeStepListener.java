package com.kafeihu.task.listener;

import com.kafeihu.task.core.TaskExecution;

/**
 * date: 2022/8/22 16:03
 * 复合listener，使用者可以用此监听所有Task框架中的执行过程
 *
 * @author kroulzhang
 */
public class CompositeStepListener implements IProcessListener, IWriterListener, IReaderListener, IHandleListener {


    @Override
    public void beforeProcess(TaskExecution execution) {

    }

    @Override
    public void afterProcess(TaskExecution execution) {

    }

    @Override
    public void onProcessError(TaskExecution execution, Exception e) {

    }

    @Override
    public void beforeHandle(TaskExecution execution) {

    }

    @Override
    public void afterHandle(TaskExecution execution) {

    }

    @Override
    public void onHandleError(TaskExecution execution, Exception e) {

    }

    @Override
    public void beforeExecute(TaskExecution execution) {
    }

    @Override
    public void afterExecute(TaskExecution execution) {
    }

    @Override
    public void onExecuteError(TaskExecution execution, Exception e) {
    }

    @Override
    public void beforeReader(TaskExecution execution) {

    }

    @Override
    public void afterReader(TaskExecution execution) {

    }

    @Override
    public void onReaderError(TaskExecution execution, Exception e) {

    }

    @Override
    public void beforeWrite(TaskExecution execution) {

    }

    @Override
    public void afterWrite(TaskExecution execution) {

    }

    @Override
    public void onWriteError(TaskExecution execution, Exception e) {

    }
}
