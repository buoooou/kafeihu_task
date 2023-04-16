package com.kafeihu.task.builder;

import com.kafeihu.task.core.ITaskExecutor;
import com.kafeihu.task.core.UniDependentTask;
import com.kafeihu.task.dependent.IDependentEvaluator;
import com.kafeihu.task.listener.IListener;
import com.kafeihu.task.process.DefaultProcessTaskExecutor;
import com.kafeihu.task.process.FreeProcessTaskExecutor;
import com.kafeihu.task.process.IProcess;
import com.kafeihu.task.reader.IDataReader;
import com.kafeihu.task.writer.IDataWriter;
import com.kafeihu.task.core.IStep;
import com.kafeihu.task.core.TaskParameters;
import com.kafeihu.task.reader.IMultiDataReader;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.lang.NonNull;


/**
 * date:  2022/4/26 19:22
 * Process模式生成器
 *
 * @author kroulzhang
 */
public class TaskProcessBuilder<R, T> {

    protected final Logger logger = LoggerFactory.getLogger(this.getClass());
    private List<IListener> listeners;
    //依赖器
    private List<IDependentEvaluator> dependentEvaluators;

    //全局预处理process
    private IProcess<? super R, ? extends T> preProcess;

    private ITaskExecutor<R, T> executor;

    private Queue<IStep> steps = new ConcurrentLinkedQueue();

    private TaskParameters taskParameters;

    private boolean skipFault = false;

    public TaskProcessBuilder() {
    }

    /***
     * 添加多个依赖器
     * @param dependentEvaluators
     * @return
     */
    public TaskProcessBuilder<R, T> dependentEvaluator(List<IDependentEvaluator> dependentEvaluators) {
        if (this.dependentEvaluators == null) {
            this.dependentEvaluators = new ArrayList<>();
        }
        this.dependentEvaluators.addAll(dependentEvaluators);
        return this;
    }


    public TaskProcessBuilder<R, T> executor(@NonNull ITaskExecutor<R, T> executor) {
        this.executor = executor;
        return this;
    }

    public TaskProcessBuilder<R, T> parameters(@NonNull TaskParameters taskParameters) {
        this.taskParameters = taskParameters;
        return this;
    }

    /***
     * 添加reader
     * @param dataReader
     * @return
     */
    public TaskProcessBuilder<R, T> reader(@NonNull IDataReader<? extends T> dataReader) {
        this.steps.add(dataReader);
        return this;
    }

    /***
     * 添加reader
     * @param dataReader
     * @return
     */
    public TaskProcessBuilder<R, T> reader(@NonNull IMultiDataReader<? extends T> dataReader) {
        this.steps.add(dataReader);
        return this;
    }

    /***
     * 添加reader
     * @param dataReader
     * @return
     */
    public TaskProcessBuilder<R, T> reader(@NonNull List<IDataReader<? extends T>> dataReader) {
        this.steps.addAll(dataReader);
        return this;
    }

    /***
     * 添加writer
     * @param dataWriter
     * @return
     */
    public TaskProcessBuilder<R, T> writer(@NonNull IDataWriter<? super R> dataWriter) {
        this.steps.add(dataWriter);
        return this;
    }

    /***
     * 添加预处理
     * @param processe
     * @return
     */
    @Deprecated
    public TaskProcessBuilder<R, T> preProcess(@NonNull IProcess<? super R, ? extends T> processe) {
        this.preProcess = processe;
        return this;
    }

    public TaskProcessBuilder<R, T> listener(@NonNull IListener listener) {
        if (this.listeners == null) {
            this.listeners = new ArrayList<>();
        }
        this.listeners.add(listener);
        return this;
    }

    /***
     * 跳过异常
     * @return
     */
    public TaskProcessBuilder<R, T> skipFault() {
        this.skipFault = true;
        return this;
    }

    /***
     * 自由组合模式
     * @return
     */
    public TaskProcessBuilder<R, T> free() {
        this.executor(new FreeProcessTaskExecutor<R, T>());
        return this;
    }

    /***
     * 添加执行处理器process链
     * @param processes
     * @return
     */
    public TaskProcessBuilder<R, T> process(@NonNull IProcess<? super R, ? extends T>... processes) {
        this.steps.addAll(Arrays.asList(processes));
        return this;
    }

    /***
     * 生成Task
     * @return
     */
    public UniDependentTask<R, T> builder() {
        UniDependentTask<R, T> task = new UniDependentTask();
        if (this.executor == null) {
            this.executor = new DefaultProcessTaskExecutor();
        }
        if (listeners != null) {
            for (IListener listener : listeners) {
                this.executor.registerListener(listener.getClass(), listener);
            }
        }
        for (IStep step : steps) {
            this.executor.addStep(step.getClass(), step);
        }
        this.executor.setSkipFault(this.skipFault);
        if (this.taskParameters != null) {
            task.setTaskParameters(this.taskParameters);
        }
        task.setTaskExecutor(executor);
        //todo 兼容特殊处理
        if (this.preProcess != null) {
            this.executor.addStep(Deprecated.class, this.preProcess);
        }
        //todo 兼容老的概念
        if (this.dependentEvaluators != null) {
            task.addDependentEvaluators(this.dependentEvaluators);
        }

        logger.info("Task:[{}] build success", task);
        return task;
    }

}
