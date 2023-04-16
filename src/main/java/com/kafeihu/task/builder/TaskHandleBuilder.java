package com.kafeihu.task.builder;


import com.kafeihu.task.core.ITaskExecutor;
import com.kafeihu.task.core.UniDependentTask;
import com.kafeihu.task.dependent.IDependentEvaluator;
import com.kafeihu.task.listener.IListener;
import com.kafeihu.task.handle.DefaultHandleTaskExecutor;
import com.kafeihu.task.handle.IHandle;

import java.util.ArrayList;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.lang.NonNull;

/**
 * date:  2022/4/26 19:22
 * Handle模式生成器
 *
 * @author kroulzhang
 */
public class TaskHandleBuilder<R, T> {

    protected final Logger logger = LoggerFactory.getLogger(this.getClass());

    //handle模式处理器
    private IHandle handle;
    private List<IListener> listeners;

    //依赖执行器
    private List<IDependentEvaluator> dependentEvaluators;

    private ITaskExecutor<R, T> executor;

    /***
     * 添加多个依赖执行器
     * @param dependentEvaluators
     * @return
     */
    public TaskHandleBuilder<R, T> dependentEvaluator(List<IDependentEvaluator> dependentEvaluators) {
        if (this.dependentEvaluators == null) {
            this.dependentEvaluators = new ArrayList<>();
        }
        this.dependentEvaluators.addAll(dependentEvaluators);
        return this;
    }

    /***
     * 添加handle
     * @param handle
     * @return
     */
    public TaskHandleBuilder<R, T> handle(@NonNull IHandle handle) {
        this.handle = handle;
        return this;
    }

    public TaskHandleBuilder<R, T> listener(@NonNull IListener listener) {
        if (this.listeners == null) {
            this.listeners = new ArrayList<>();
        }
        this.listeners.add(listener);
        return this;
    }

    public TaskHandleBuilder<R, T> executor(@NonNull ITaskExecutor<R, T> executor) {
        this.executor = executor;
        return this;
    }

    /***
     * 生成Task
     * @return
     */
    public UniDependentTask builder() {

        if (this.executor == null) {
            this.executor = new DefaultHandleTaskExecutor();
        }
        if (this.handle != null) {
            this.executor.addStep(IHandle.class, handle);
        }
        if (listeners != null) {
            for (IListener listener : listeners) {
                this.executor.registerListener(listener.getClass(), listener);
            }
        }
        UniDependentTask task = new UniDependentTask();
        task.setTaskExecutor(this.executor);
        if (this.dependentEvaluators != null) {
            task.addDependentEvaluators(this.dependentEvaluators);
        }
        logger.info("Task:[{}] build success", task);
        return task;
    }

}
