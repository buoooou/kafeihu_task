package com.kafeihu.task.builder;


import com.kafeihu.task.dependent.IDependentEvaluator;
import com.kafeihu.task.process.FreeProcessTaskExecutor;
import com.kafeihu.task.core.TaskParametersIncrementer;
import com.kafeihu.task.handle.IHandle;
import com.kafeihu.task.reader.IDataReader;
import com.kafeihu.task.reader.IMultiDataReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.springframework.lang.NonNull;

/**
 * date:  2022/3/31 15:16
 * 通用构造器，通过该构造器可以生成所需要的Task任务
 * Task有两种模式，可以通过该构造器来做统一入口后根据不用的模式，选择
 * TaskHandleBuilder或者TaskProcesssBuilder来生成两种模式的Task
 *
 * @author kroulzhang
 */
public class SimpleTaskBuilder<R, T> {

    //依赖执行器
    private List<IDependentEvaluator> dependentEvaluators;

    private SimpleTaskBuilder() {
    }

    //生成TaskBuilder
    public static SimpleTaskBuilder getInstance() {
        return new SimpleTaskBuilder();
    }

    /***
     * 添加多个依赖执行器
     * @param dependentEvaluators
     * @return
     */
    @Deprecated
    public SimpleTaskBuilder dependentEvaluator(IDependentEvaluator... dependentEvaluators) {
        if (this.dependentEvaluators == null) {
            this.dependentEvaluators = new ArrayList<>();
        }
        this.dependentEvaluators.addAll(Arrays.asList(dependentEvaluators));
        return this;
    }

    /***
     * 添加单个依赖执行器
     * @param dependentEvaluator
     * @return
     */
    @Deprecated
    public SimpleTaskBuilder dependentEvaluator(IDependentEvaluator dependentEvaluator) {
        if (this.dependentEvaluators == null) {
            this.dependentEvaluators = new ArrayList<>();
        }
        this.dependentEvaluators.add(dependentEvaluator);
        return this;
    }

    /***
     * 自由组合模式
     * @return
     */
    public <R, T> TaskProcessBuilder<R, T> free() {
        TaskProcessBuilder<R, T> taskProcesssBuilder = new TaskProcessBuilder<R, T>();
        taskProcesssBuilder.executor(new FreeProcessTaskExecutor<R, T>());
        return taskProcesssBuilder;
    }


    /***
     * 添加参数
     * @return
     */
    public <R, T> TaskProcessBuilder<R, T> parameter(TaskParametersIncrementer incrementer) {
        TaskProcessBuilder<R, T> taskProcesssBuilder = new TaskProcessBuilder<R, T>();
        taskProcesssBuilder.parameters(incrementer.getNext());
        return taskProcesssBuilder;
    }

    /***
     * 添加reader
     * @param dataReader
     * @return
     */
    public <R, T> TaskProcessBuilder<R, T> reader(IDataReader<? extends T> dataReader) {
        TaskProcessBuilder<R, T> taskProcesssBuilder = new TaskProcessBuilder<R, T>();
        taskProcesssBuilder.reader(dataReader);
        if (this.dependentEvaluators != null) {
            taskProcesssBuilder.dependentEvaluator(this.dependentEvaluators);
        }

        return taskProcesssBuilder;
    }

    /***
     * 添加reader
     * @param dataReader
     * @return
     */
    public <R, T> TaskProcessBuilder<R, T> reader(@NonNull IMultiDataReader<? extends T> dataReader) {
        TaskProcessBuilder<R, T> taskProcesssBuilder = new TaskProcessBuilder<>();
        taskProcesssBuilder.reader(dataReader);
        if (this.dependentEvaluators != null) {
            taskProcesssBuilder.dependentEvaluator(this.dependentEvaluators);
        }
        return taskProcesssBuilder;
    }

    /***
     * 添加reader
     * @param dataReader
     * @return
     */
    public <R, T> TaskProcessBuilder<R, T> reader(IDataReader<? extends T>... dataReader) {
        TaskProcessBuilder<R, T> taskProcesssBuilder = new TaskProcessBuilder<>();
        taskProcesssBuilder.reader(Arrays.asList(dataReader));
        if (this.dependentEvaluators != null) {
            taskProcesssBuilder.dependentEvaluator(this.dependentEvaluators);
        }

        return taskProcesssBuilder;
    }

    /***
     * 添加handle
     * @param handle
     * @return
     */
    public TaskHandleBuilder<R, T> handle(IHandle handle) {
        TaskHandleBuilder<R, T> taskHandleBuilder = new TaskHandleBuilder();
        taskHandleBuilder.handle(handle);
        if (this.dependentEvaluators != null) {
            taskHandleBuilder.dependentEvaluator(this.dependentEvaluators);
        }

        return taskHandleBuilder;
    }

}
