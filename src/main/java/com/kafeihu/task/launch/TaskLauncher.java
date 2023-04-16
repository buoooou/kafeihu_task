package com.kafeihu.task.launch;

import com.kafeihu.task.core.ITask;
import com.kafeihu.task.core.TaskParameters;
import org.springframework.core.task.TaskExecutor;

/**
 * date: 2022/11/3 13:58
 *
 * @author kroulzhang
 */
public interface TaskLauncher {

    /***
     * Task 执行：同步、异步
     * @param task
     * @param taskName
     * @param taskParameter
     */
    void run(ITask task, String taskName, TaskParameters taskParameter);

    void setTaskExecutor(TaskExecutor taskExecutor);
}
