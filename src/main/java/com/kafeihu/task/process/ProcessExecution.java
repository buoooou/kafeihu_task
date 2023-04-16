package com.kafeihu.task.process;

import com.kafeihu.task.core.ExecutionContext;
import com.kafeihu.task.core.IExecution;
import com.kafeihu.task.core.TaskExecution;
import com.kafeihu.task.core.constant.ExitStatus;
import com.kafeihu.task.core.constant.TaskConstant;
import com.kafeihu.task.core.constant.TaskStatus;
import com.kafeihu.task.core.statistic.ProcessStatistic;

/**
 * date:   2022/4/19 18:32
 *
 * ProcessExecution process执行过程
 * ProcessExecution = context+ 统计信息
 *
 * @author kroulzhang
 */
public class ProcessExecution implements IExecution {

    private final TaskExecution taskExecution;

    private volatile ExecutionContext executionContext = new ExecutionContext();

    private ProcessStatistic statistic = new ProcessStatistic();

    public ProcessExecution(TaskExecution taskExecution) {
        this.taskExecution = taskExecution;
    }

    public void setExecuteBeginTime(String name) {
        if (getExecutionContext().get(TaskConstant.RUNNING_NUM) == null) {
            int num = (int) taskExecution.getExecutionContext().get(TaskConstant.RUNNING_NUM);
            statistic.setExecuteBeginTime(name + "_" + num, System.currentTimeMillis());
            getExecutionContext().put(TaskConstant.RUNNING_NUM, num);
            taskExecution.getExecutionContext().put(TaskConstant.RUNNING_NUM, ++num);
        } else {
            int num = (int) getExecutionContext().get(TaskConstant.RUNNING_NUM);
            statistic.setExecuteBeginTime(name + "_" + num, System.currentTimeMillis());
        }
    }

    public void setExecuteEndTime(String name) {
        int num = (int) getExecutionContext().get(TaskConstant.RUNNING_NUM);
        statistic.setExecuteEndTime(name + "_" + num, System.currentTimeMillis());
    }

    public void setSum(int sum) {
        statistic.setSum(sum);
    }

    public void addSuccess() {
        statistic.addSuccess(1);
    }

    public void addRun() {
        statistic.addRun(1);
    }

    public void addFail() {
        statistic.addFail(1);
    }

    /***
     * 获取上下文
     * @return
     */
    public ExecutionContext getExecutionContext() {
        return executionContext;
    }

    /***
     * 设置上下文
     * @param executionContext
     */
    public void setExecutionContext(ExecutionContext executionContext) {
        this.executionContext = executionContext;
    }

    public TaskExecution getTaskExecution() {
        return taskExecution;
    }

    @Override
    public String toString() {
        return "ProcessExecution{"
                + "taskExecution=" + taskExecution.getTaskName()
                + ", executionContext=" + executionContext
                + ", statistic=" + statistic
                + '}';
    }

    @Override
    public String statistic() {
        return "{"
                + "\"processStatistic\":" + statistic.statistic()
                + '}';
    }

    @Override
    public TaskStatus getTaskStatus() {
        return getTaskExecution().getTaskStatus();
    }

    @Override
    public ExitStatus getExitStatus() {
        return getTaskExecution().getExitStatus();
    }
}
