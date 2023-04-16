package com.kafeihu.task.handle;


import com.kafeihu.task.listener.IListener;
import com.kafeihu.task.listener.LogListener;
import com.kafeihu.task.core.IStep;
import com.kafeihu.task.core.TaskExecution;
import com.kafeihu.task.launch.TaskCommandParameters;

/**
 * date:  2022/4/26 18:54
 * Handle模式
 * Handle全自定义执行：
 * 编排框架不限定内部执行单元，自由发挥
 *
 * @author kroulzhang
 */
@Deprecated
public interface IHandle extends IStep {

    @Override
    default Object run(Object object, TaskExecution taskExecution) throws Exception {
        handle(taskExecution.getHandleExecution());
        return new Object();
    }

    /***
     * 任务执行
     */
    void handle(HandleExecution execution) throws Exception;

    /**
     * 参数校验
     *
     * @return 校验结果
     */
    Boolean checkParam(TaskCommandParameters parameters);

    /**
     * 批处理 结束
     */
    default void batchEnd(HandleExecution execution) {
    }

    /***
     * 默认监听器
     * @return
     */
    @Override
    default IListener listener() {
        return new LogListener(this.getClass().getSimpleName(), "handle");
    }
}
