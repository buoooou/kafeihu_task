package com.kafeihu.task.core;

import com.kafeihu.task.core.constant.ExitStatus;
import com.kafeihu.task.core.constant.TaskStatus;

/**
 * date: 2022/8/18 17:26
 * 执行器接口
 *
 * @author kroulzhang
 */
public interface IExecution {

    String statistic();

    TaskStatus getTaskStatus();

    ExitStatus getExitStatus();
}
