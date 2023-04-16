package com.kafeihu.task.launch;

import com.kafeihu.task.core.ITask;
import com.kafeihu.task.core.TaskExecution;
import com.kafeihu.task.core.TaskParameters;
import com.kafeihu.task.core.constant.ExitStatus;
import com.kafeihu.task.core.constant.TaskStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.task.TaskExecutor;
import org.springframework.core.task.TaskRejectedException;
import org.springframework.util.Assert;

/**
 * date: 2022/11/3 14:00
 *
 * @author kroulzhang
 */
public class SimpleTaskLauncher implements TaskLauncher {

    private static final Logger logger = LoggerFactory.getLogger(SimpleTaskLauncher.class);

    private TaskExecutor taskExecutor;

    @Override
    public void run(ITask task, String taskName, TaskParameters taskParameter) {

        Assert.notNull(task, "The task must not be null.");
        Assert.notNull(taskParameter, "The taskParameters must not be null.");

        TaskExecution execution = new TaskExecution(taskParameter, taskName);
        execution.setCommandParameters(taskParameter.getTaskCommondParameters());
        execution.initialize();
        try {
            taskExecutor.execute(new Runnable() {

                @Override
                public void run() {
                    try {
                        logger.info("Task: [" + task + "] launched with the following parameters: [" + taskParameter
                                + "]");
                        task.run(execution);

                        logger.info("Task: [" + task + "] completed with the following parameters: [" + taskParameter
                                + "] and the following status: [" + execution.getExitStatus() + "]");
                    } catch (Throwable t) {
                        logger.info("Task: [" + task
                                + "] failed unexpectedly and fatally with the following parameters: [" + taskParameter
                                + "]", t);
                        rethrow(t);
                    }
                }

                private void rethrow(Throwable t) {
                    if (t instanceof RuntimeException) {
                        throw (RuntimeException) t;
                    } else if (t instanceof Error) {
                        throw (Error) t;
                    }
                    throw new IllegalStateException(t);
                }
            });

        } catch (TaskRejectedException e) {
            execution.upgradeStatus(TaskStatus.FAILED);
            if (execution.getExitStatus().equals(ExitStatus.UNKNOWN)) {
                execution.setExitStatus(ExitStatus.EXCEPTION);
            }
        }
    }

    public void setTaskExecutor(TaskExecutor taskExecutor) {
        this.taskExecutor = taskExecutor;
    }

}
