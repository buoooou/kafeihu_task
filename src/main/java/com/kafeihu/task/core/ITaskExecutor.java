package com.kafeihu.task.core;

import com.kafeihu.task.listener.IListener;

/**
 * date: 2022/8/5 14:28
 * Task执行器：包括handle模式，process模式，free模式
 *
 * @author kroulzhang
 */
public interface ITaskExecutor<R, T> {

    /***
     * 设置默认跳过异常，控制执行期间的异常处理
     * @param skipFault
     */
    default void setSkipFault(boolean skipFault) {
    }

    /***
     * task执行
     * @param execution
     * @throws Exception
     */
    void execute(TaskExecution execution) throws Exception;

    /***
     * 添加task执行过程中step
     * @param clazz
     * @param step
     */
    void addStep(Class clazz, IStep step);

    /***
     * 注册监听器
     * @param clazz
     * @param listener
     */
    void registerListener(Class clazz, IListener listener);
}
