package com.kafeihu.task.handle;

import com.kafeihu.task.core.ITaskExecutor;
import com.kafeihu.task.listener.CompositeHandleListener;
import com.kafeihu.task.listener.IHandleListener;
import com.kafeihu.task.listener.IListener;
import com.kafeihu.task.core.IStep;
import com.kafeihu.task.core.TaskExecution;
import com.kafeihu.task.core.exception.TaskHandleException;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import org.springframework.lang.NonNull;
import org.springframework.util.Assert;

/**
 * date: 2022/7/13 17:11
 * 默认handle 处理器
 *
 * @author kroulzhang
 */
@Deprecated
public class DefaultHandleTaskExecutor<R, T> implements ITaskExecutor<R, T> {

    private IHandle handle;
    private List<IHandleListener> listeners = new CopyOnWriteArrayList<>();

    @Override
    public void addStep(Class clazz, @NonNull IStep step) {
        Assert.isInstanceOf(clazz, step, "only support handle step!");
        this.handle = (IHandle) step;
    }

    @Override
    public void registerListener(Class clazz, @NonNull IListener listener) {
        if (listener instanceof IHandleListener) {
            this.listeners.add((IHandleListener) listener);
        }
    }

    /***
     * handle 任务处理模式
     * 全自定义
     * @param execution
     * @throws Exception
     */
    @Override
    public void execute(TaskExecution execution) throws TaskHandleException {
        if (handle != null) {
            HandleExecution handleExecution = execution.getHandleExecution();
            handleExecution.setSum(1);
            CompositeHandleListener listener = new CompositeHandleListener();
            if (listeners != null) {
                listener.setListeners(listeners);
            }
//            listener.register(handle.listener());
            listener.beforeHandle(execution);
            try {
                handleExecution.setExecuteBeginTime(handle.name());
                if (this.handle.checkParam(execution.getCommandParameters())) {
                    this.handle.handle(handleExecution);
                    handleExecution.addSuccess();
                }
            } catch (Exception e) {
                handleExecution.addFail();
                listener.onHandleError(execution, e);
                throw new TaskHandleException(String.format("[%s] execute failed: reason:[%s]",
                        handle.name(), e.getMessage()), e);
            } finally {
                handle.batchEnd(handleExecution);
                handleExecution.setExecuteEndTime(handle.name());
                listener.afterHandle(execution);
            }
        }
    }

}
