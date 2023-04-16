package com.kafeihu.task.process;

import com.kafeihu.task.core.ITaskExecutor;
import com.kafeihu.task.listener.IListener;
import com.kafeihu.task.listener.IProcessListener;
import com.kafeihu.task.listener.IReaderListener;
import com.kafeihu.task.listener.IWriterListener;
import com.kafeihu.task.core.IStep;
import com.kafeihu.task.core.TaskExecution;
import com.kafeihu.task.core.constant.ProcessSchema;
import com.kafeihu.task.core.exception.TaskProcessException;
import com.kafeihu.task.reader.IDataReader;
import com.kafeihu.task.writer.IDataWriter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.CopyOnWriteArrayList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.lang.NonNull;
import org.springframework.util.Assert;


/***
 * date: 2022/7/13 17:11
 *
 *
 * free模式，可自定义step的执行过程顺序
 *
 * @param <R> 写数据结构体
 * @param <T> 读数据结构体
 *
 * @author kroulzhang
 */
public class FreeProcessTaskExecutor<R, T> implements ITaskExecutor<R, T> {

    protected final Logger logger = LoggerFactory.getLogger(this.getClass());
    /***
     * 组件监听器
     */
    private List<IProcessListener> processListeners = new CopyOnWriteArrayList();
    private List<IWriterListener> writerListeners = new CopyOnWriteArrayList<>();
    private List<IReaderListener> readerListeners = new CopyOnWriteArrayList<>();

    /***
     * task执行辅助工具
     */
    private TaskExecutorHelper helper = new TaskExecutorHelper();

    /***
     * task 过程中step组件
     */
    private Queue<IStep> steps = new ConcurrentLinkedQueue();

    /***
     * 监视器注册
     * @param clazz 类名
     * @param listener 监视器
     */
    @Override
    public void registerListener(Class clazz, @NonNull IListener listener) {
        if (listener instanceof IProcessListener) {
            processListeners.add((IProcessListener) listener);
        }
        if (listener instanceof IWriterListener) {
            writerListeners.add((IWriterListener) listener);
        }
        if (listener instanceof IReaderListener) {
            readerListeners.add((IReaderListener) listener);
        }
    }


    /***
     * 任务自由组合模式：
     * todo 优化
     * @throws Exception
     */
    @Override
    public void execute(TaskExecution execution) throws Exception {
        List<Object> result = new ArrayList<>();
        for (IStep step : steps) {
            if (step instanceof IDataReader) {
                result = helper.readItem(readerListeners, (IDataReader) step, execution);

            } else if (step instanceof IDataWriter) {
                Assert.notNull(result, "writer data not be null");
                helper.writeItem(writerListeners, (IDataWriter) step, result, execution);

            } else if (step instanceof IProcess) {
                IProcess processor = (IProcess<R, T>) step;
                if (processor.schema().equals(ProcessSchema.ALL_BY_ONE_PROCESS)) {
                    result = (List<Object>) helper.processItem(processListeners, processor, result,
                            execution.getProcessExecution());
                } else if (processor.schema().equals(ProcessSchema.ONE_BY_ONE_PROCESS)) {
                    result = (List<Object>) process((List<T>) result, processor, execution);
                } else {
                    throw new TaskProcessException("not support schema:" + processor.schema().getValue());
                }
            } else {
                throw new TaskProcessException("not support step:" + step.name());
            }
        }
    }

    /***
     * 数据处理，处理模式：单条处理
     * todo 条数异常跳过
     * @param datas
     * @param execution
     * @return
     */
    private List<R> process(List<T> datas, IProcess processor, TaskExecution execution) throws Exception {
        List<R> result = new ArrayList<>();
        ProcessExecution processExecution = execution.getProcessExecution();

        Iterator<T> iterator = datas.iterator();

        while (iterator.hasNext()) {
            T data = iterator.next();
            R res = (R) helper.processItem(processListeners, processor, data, processExecution);
            if (res instanceof Collection) {
                result.addAll((Collection<? extends R>) res);
            } else {
                result.add(res);
            }
            processExecution.getExecutionContext().clear();
        }
        return result;
    }

    @Override
    public void addStep(Class clazz, @NonNull IStep step) {
        steps.add(step);
    }
}
