package com.kafeihu.task.core;

import com.kafeihu.task.core.constant.ExitStatus;
import com.kafeihu.task.core.constant.TaskStatus;
import com.kafeihu.task.core.statistic.BaseStatistic;
import com.kafeihu.task.handle.HandleExecution;
import com.kafeihu.task.handle.IHandle;
import com.kafeihu.task.launch.TaskCommandParameters;
import com.kafeihu.task.process.IProcess;
import com.kafeihu.task.process.ProcessExecution;
import com.kafeihu.task.reader.IDataReader;
import com.kafeihu.task.writer.IDataWriter;
import com.kafeihu.task.core.event.AbstractTaskEvent;
import com.kafeihu.task.core.event.HandleEvent;
import com.kafeihu.task.core.event.ProcessEvent;
import com.kafeihu.task.core.event.ReaderEvent;
import com.kafeihu.task.core.event.WriterEvent;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * date: 2022/4/18 11:24
 * TaskExecution task执行过程
 * 包括context、statistic、event、processExecution、handleExecution
 *
 * @author kroulzhang
 */
public class TaskExecution implements IExecution {

    private final String taskName;
    /***
     * Task 命令行参数
     */
    private final TaskParameters taskParameters;

    /***
     * task Event管理mapper
     */
    private Map<Class, AbstractTaskEvent> taskEventMap = new ConcurrentHashMap<>();

    /***
     * process执行过程
     */
    private ProcessExecution processExecution = new ProcessExecution(this);

    /***
     * handle执行过程
     */
    private HandleExecution handleExecution = new HandleExecution(this);

    /***
     * reader统计信息
     */
    private BaseStatistic readerStatistic = new BaseStatistic();
    /***
     * writer统计信息
     */
    private BaseStatistic writerStatistic = new BaseStatistic();

    private volatile TaskStatus status = TaskStatus.STARTING;

    private volatile ExitStatus exitStatus = ExitStatus.INIT;

    private volatile ExecutionContext executionContext = new ExecutionContext();

    private TaskCommandParameters taskCommandParameters;

    public TaskExecution(TaskParameters taskParameters, String taskName) {
        this.taskParameters = taskParameters;
        this.taskName = taskName;
    }

    public void setEvent(Class clazz, AbstractTaskEvent event) {
        this.taskEventMap.put(clazz, event);
    }

    public void setEvent(IStep step, AbstractTaskEvent event) {
        if (step instanceof IDataReader) {
            this.taskEventMap.put(ReaderEvent.class, event);
        } else if (step instanceof IHandle) {
            this.taskEventMap.put(HandleEvent.class, event);
        } else if (step instanceof IDataWriter) {
            this.taskEventMap.put(WriterEvent.class, event);
        } else if (step instanceof IProcess) {
            this.taskEventMap.put(ProcessEvent.class, event);
        }
    }

    public WriterEvent getWriterEvent() {
        return (WriterEvent) taskEventMap.get(WriterEvent.class);
    }

    public ReaderEvent getReaderEvent() {
        return (ReaderEvent) taskEventMap.get(ReaderEvent.class);
    }

    public HandleEvent getHandleEvent() {
        return (HandleEvent) taskEventMap.get(HandleEvent.class);
    }

    public ProcessEvent getProcessEvent() {
        return (ProcessEvent) taskEventMap.get(ProcessEvent.class);
    }


    /***
     * TaskExecution 初始化
     */
    public void initialize() {

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

    public TaskCommandParameters getCommandParameters() {
        return this.taskCommandParameters;
    }

    public void setCommandParameters(TaskCommandParameters taskCommandParameters) {
        this.taskCommandParameters = taskCommandParameters;
    }

    public TaskParameters getTaskParameters() {
        return this.taskParameters;
    }

    public ProcessExecution getProcessExecution() {
        return this.processExecution;
    }

    public HandleExecution getHandleExecution() {
        return this.handleExecution;
    }

    public void setHandleExecution(HandleExecution handleExecution) {
        this.handleExecution = handleExecution;
    }

    public void setExecuteBeginTime(String name, IStep step) {
        if (step instanceof IDataWriter) {
            writerStatistic.setExecuteBeginTime(name, System.currentTimeMillis());
        } else if (step instanceof IDataReader) {
            readerStatistic.setExecuteBeginTime(name, System.currentTimeMillis());
        }
    }

    public void setExecuteEndTime(String name, IStep step) {
        if (step instanceof IDataWriter) {
            writerStatistic.setExecuteEndTime(name, System.currentTimeMillis());
        } else if (step instanceof IDataReader) {
            readerStatistic.setExecuteEndTime(name, System.currentTimeMillis());
        }
    }

    public void setSum(int sum, IStep step) {
        if (step instanceof IDataWriter) {
            writerStatistic.setSum(sum);
        } else if (step instanceof IDataReader) {
            readerStatistic.setSum(sum);
        }
    }

    public void addSuccess(IStep step) {
        if (step instanceof IDataWriter) {
            writerStatistic.addSuccess(1);
        } else if (step instanceof IDataReader) {
            readerStatistic.addSuccess(1);
        }
    }

    public void addFail(IStep step) {
        if (step instanceof IDataWriter) {
            writerStatistic.addFail(1);
        } else if (step instanceof IDataReader) {
            readerStatistic.addFail(1);
        }
    }

    @Override
    public String toString() {
        return "TaskExecution{"
                + "taskName='" + taskName + '\''
                + ", taskParameters=" + taskParameters
                + ", taskEventMap=" + taskEventMap
                + ", processExecution=" + processExecution
                + ", handleExecution=" + handleExecution
                + ", readerStatistic=" + readerStatistic
                + ", writerStatistic=" + writerStatistic
                + ", status=" + status
                + ", executionContext=" + executionContext
                + ", taskCommandParameters=" + taskCommandParameters
                + '}';
    }

    /***
     * 统计信息
     * @return
     */
    @Override
    public String statistic() {

        return "["
                + "{\"readerStatistic\":" + readerStatistic.statistic()
                + "},{\"writerStatistic\":" + writerStatistic.statistic()
                + "},{\"processExecution\":" + processExecution.statistic()
                + "},{\"handleExecution\":" + handleExecution.statistic()
                + "}]";
    }

    public void setStatus(TaskStatus status) {
        this.status = status;
    }

    /***
     * 更新Task状态
     * @param status
     */
    public void upgradeStatus(TaskStatus status) {
        this.status = this.status.upgradeTo(status);
    }

    /***
     * 设置task 退出状态
     * @param status
     */
    public void setExitStatus(ExitStatus status) {
        this.exitStatus = status;
    }

    @Override
    public TaskStatus getTaskStatus() {
        return status;
    }

    @Override
    public ExitStatus getExitStatus() {
        return exitStatus;
    }


    public String getTaskName() {
        return this.taskName;
    }
}
