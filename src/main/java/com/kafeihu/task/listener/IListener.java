package com.kafeihu.task.listener;

import com.kafeihu.task.core.TaskExecution;

/**
 * date: 2022/4/20 17:25
 * listener
 *
 * @author kroulzhang
 */
public interface IListener {

    /***
     * 前置监听
     * @param execution
     */
    void beforeExecute(TaskExecution execution);

    /***
     * 后置监听
     * @param execution
     */
    void afterExecute(TaskExecution execution);

    /***
     * 异常监听
     * @param execution
     * @param e
     */
    void onExecuteError(TaskExecution execution, Exception e);
}
