package com.kafeihu.task.handle;

import com.kafeihu.task.core.ExecutionContext;
import com.kafeihu.task.core.IExecution;
import com.kafeihu.task.core.TaskExecution;
import com.kafeihu.task.core.constant.ExitStatus;
import com.kafeihu.task.core.constant.TaskStatus;
import com.kafeihu.task.core.statistic.BaseStatistic;

/**
 * date: 2022/4/19 18:32
 * 默认handle 执行器
 * HandleExecution handle模式执行过程
 * HandleExecution= context+ 统计信息
 *
 * @author kroulzhang
 */
@Deprecated
public class HandleExecution implements IExecution {

    private final TaskExecution taskExecution;
    private BaseStatistic statistic = new BaseStatistic();
    private volatile ExecutionContext executionContext = new ExecutionContext();


    public HandleExecution(TaskExecution taskExecution) {
        this.taskExecution = taskExecution;
    }

    public void setExecuteBeginTime(String name) {
        statistic.setExecuteBeginTime(name, System.currentTimeMillis());
    }

    public void setExecuteEndTime(String name) {
        statistic.setExecuteEndTime(name, System.currentTimeMillis());
    }

    public void setSum(int sum) {
        statistic.setSum(sum);
    }

    public void addSuccess() {
        statistic.addSuccess(1);
    }

    public void addFail() {
        statistic.addFail(1);
    }

    /***
     * 获取上下文
     * @return 上下文
     */
    public ExecutionContext getExecutionContext() {
        return executionContext;
    }

    /***
     * 设置上下文
     * @param executionContext 上下文
     */
    public void setExecutionContext(ExecutionContext executionContext) {
        this.executionContext = executionContext;
    }

    public TaskExecution getTaskExecution() {
        return taskExecution;
    }

    @Override
    public String toString() {
        return "HandleExecution{"
                + "taskExecution=" + taskExecution.getTaskName()
                + ", statistic=" + statistic
                + ", executionContext=" + executionContext
                + '}';
    }

    @Override
    public String statistic() {
        return "{"
                + "\"handleStatistic\":" + statistic.statistic()
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
