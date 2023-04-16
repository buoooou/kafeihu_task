package com.kafeihu.task.process;

import com.kafeihu.task.listener.IListener;
import com.kafeihu.task.listener.LogListener;
import com.kafeihu.task.core.IStep;
import com.kafeihu.task.core.TaskExecution;
import com.kafeihu.task.core.constant.ProcessSchema;

/**
 * date:  2022/4/17 20:20
 *
 * @param <R> 写数据结构体
 * @param <T> 读数据结构体
 * @author kroulzhang
 */
public interface IProcess<R, T> extends IStep<R, T> {

    /***
     *  多个process可以通过上下文context进行参数传递
     *  任务执行
     * @param t 入参
     * @return R
     * @throws Exception
     */
    R process(T t, ProcessExecution execution) throws Exception;

    @Override
    default R run(T object, TaskExecution taskExecution) throws Exception {
        return process(object, taskExecution.getProcessExecution());
    }

    /***
     * process 执行模式
     * @return process模式
     */
    default ProcessSchema schema() {
        return ProcessSchema.ONE_BY_ONE_PROCESS;
    }

    /***
     * 默认监听器
     * @return 监视器
     */
    @Override
    default IListener listener() {
        return new LogListener(this.getClass().getSimpleName(), "process");
    }
}
