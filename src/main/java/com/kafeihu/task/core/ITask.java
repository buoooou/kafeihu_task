package com.kafeihu.task.core;

import com.kafeihu.task.core.constant.ExitStatus;

/**
 * date: 2022/3/25 14:51
 * 任务接口
 *
 * @author kroulzhang
 */
public interface ITask {

    /***
     * 任务执行
     */
    ExitStatus run(TaskExecution context);

    TaskParameters getTaskParameters();

}
