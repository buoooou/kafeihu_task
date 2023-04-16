package com.kafeihu.task.core;


import com.kafeihu.task.core.constant.ExitStatus;
import com.kafeihu.task.core.constant.TaskStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * date: 2022/4/17 22:10
 *
 * 通用执行Task，包括process 和 handle两种模式<br/>
 * v0.0.1版本：reader-convertor-calculator...-convertor-writer<br/>
 * 单数据源读取，转换，多个计算，转换，单次写入<br/>
 * v0.0.2版本：reader-process...-writer<br/>
 * 单数据源读取，多个处理，单次写入<br/>
 * v1.0.1版本：reader...-preProcess-process...-writer<br/>
 * 多数据源读取，前序处理，多个处理，单次写入<br/>
 * v1.0.2版本：移除preTask
 * Task框架更关注Task本身的编排，Task维度的编排调度，更多是调度系统需要考虑的功能
 *
 * v2.0.0版本：process流程编排：并行，串行，先后等
 * TODO：
 * 技术抽象：
 * <pre>
 *   1.产品概念的reader，process，preProcess，writer抽象为统一基础process处理器
 *   2.根据基层process处理器，上层实现reader、process、writer等
 *   3.增加分支流程控制：基层process处理的并行，串行，前后等
 * </pre>
 *
 * @author kroulzhang
 */
public abstract class AbstractTask<R, T> implements ITask {

    protected final Logger logger = LoggerFactory.getLogger(this.getClass());

    private ITaskExecutor<R, T> taskExecutor;

    public ITaskExecutor<R, T> getTaskExecutor() {
        return taskExecutor;
    }

    public void setTaskExecutor(ITaskExecutor<R, T> taskExecutor) {
        this.taskExecutor = taskExecutor;
    }

    /***
     * 任务执行
     * @param execution 任务执行器
     * @return ExitStatus 任务返回状态
     */
    @Override
    public ExitStatus run(TaskExecution execution) {
        //初始返回状态
        execution.setExitStatus(ExitStatus.INIT);
        try {
            //任务当前状态：开始
            execution.upgradeStatus(TaskStatus.STARTING);
            beforeExecute(execution);
            execution.setExitStatus(execute(execution));
            //任务当前状态：执行完毕
            execution.upgradeStatus(TaskStatus.STARTED);
        } catch (Throwable e) {
            execution.setExitStatus(ExitStatus.EXCEPTION);
            //任务当前状态：执行失败
            execution.upgradeStatus(TaskStatus.FAILED);
            handleException(execution, e);
        } finally {
            // 任务执行后处理
            execution.upgradeStatus(TaskStatus.STOPPING);
            try {
                afterExecute(execution.getExitStatus(), execution);
            } catch (Exception e) {
                logger.error("execute afterExecute error", e);
            } finally {
                execution.upgradeStatus(TaskStatus.STOPPED);
            }
        }
        return execution.getExitStatus();
    }

    /***
     * 任务前序执行处理，校验，监控等
     * @param execution
     */
    abstract void beforeExecute(TaskExecution execution);

    /***
     * 任务执行处理
     * @param execution
     * @throws Exception
     */
    abstract ExitStatus execute(TaskExecution execution) throws Exception;

    void handleException(TaskExecution execution, Throwable e) {

        String msg = String.format("job{TaskPara:[%s]} run exception: [%s]",
                execution.getTaskParameters(), e.getMessage());
        logger.error(msg, e);

    }

    /***
     * 任务执行完毕后续处理，监控
     * @param execution
     */
    abstract void afterExecute(ExitStatus status, TaskExecution execution);

    /***
     * process 任务处理模式 合并 handle 任务处理模式
     * 暂时只支持模式：批量读取数据--单条数据经过多个process处理--批量写入数据
     * 1.读取数据
     * 2.处理数据 R, T （可支持无需处理数据，直接写入，需满足T extends of R）
     * 3.写入数据
     * @throws Exception
     */
    protected void doExecute(TaskExecution execution) throws Exception {
        taskExecutor.execute(execution);
    }


    @Override
    public String toString() {
        return "AbstractTask{"
                + "taskExecutor=" + taskExecutor
                + '}';
    }
}
