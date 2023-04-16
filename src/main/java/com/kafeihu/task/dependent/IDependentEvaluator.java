package com.kafeihu.task.dependent;


import com.kafeihu.task.core.TaskExecution;
import com.kafeihu.task.listener.IListener;
import com.kafeihu.task.listener.LogListener;

/**
 * date:  2022/4/27 15:30
 * Task执行前期的依赖项
 *
 * @author kroulzhang
 */
@Deprecated
public interface IDependentEvaluator {

    /**
     * 评估一个依赖条件是否已经得到满足
     *
     * @param execution
     * @return
     */
    boolean ready(TaskExecution execution);


    /***
     * 默认监听器
     * @return
     */
    default IListener listener() {
        return new LogListener(this.getClass().getSimpleName(), "dependent");
    }
}
