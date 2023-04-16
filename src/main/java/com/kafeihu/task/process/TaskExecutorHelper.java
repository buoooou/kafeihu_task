package com.kafeihu.task.process;

import com.kafeihu.task.core.TaskExecution;
import com.kafeihu.task.core.constant.ExitStatus;
import com.kafeihu.task.core.event.ProcessEvent;
import com.kafeihu.task.core.event.ReaderEvent;
import com.kafeihu.task.core.event.WriterEvent;
import com.kafeihu.task.core.exception.TaskProcessException;
import com.kafeihu.task.core.exception.TaskReaderException;
import com.kafeihu.task.core.exception.TaskWriterException;
import com.kafeihu.task.listener.CompositeProcessListener;
import com.kafeihu.task.listener.CompositeReaderListener;
import com.kafeihu.task.listener.CompositeWriterListener;
import com.kafeihu.task.listener.IProcessListener;
import com.kafeihu.task.listener.IReaderListener;
import com.kafeihu.task.listener.IWriterListener;
import com.kafeihu.task.reader.IDataReader;
import com.kafeihu.task.writer.IDataWriter;
import java.util.ArrayList;
import java.util.List;

/**
 * date:   2022/7/19 11:18
 *
 * task执行过程辅助工具
 *
 * @author kroulzhang
 */
public class TaskExecutorHelper<R, T> {

    /***
     * process 执行原子
     * @param listeners 监视器
     * @param processor 执行器
     * @param input 输入数据
     * @param execution Task执行器
     * @param <R> 返回数据类型泛型
     * @return 返回数据结构
     * @throws TaskProcessException
     */
    public <R> Object processItem(List<IProcessListener> listeners, IProcess<R, T> processor, Object input,
                                  ProcessExecution execution)
            throws TaskProcessException {
        CompositeProcessListener listener = new CompositeProcessListener();
        if (listeners != null) {
            listener.setListeners(listeners);
        }
//        listener.register(processor.listener());
        execution.getTaskExecution().setEvent(ProcessEvent.class, new ProcessEvent(input));
        listener.beforeProcess(execution.getTaskExecution());
        Object object = input;
        try {
            execution.addRun();
            execution.setExecuteBeginTime(processor.name());
            object = processor.process((T) input, execution);
            execution.addSuccess();
        } catch (Exception e) {
            execution.addFail();
            execution.getTaskExecution().setExitStatus(ExitStatus.EXCEPTION);
            listener.onProcessError(execution.getTaskExecution(), e);
            throw new TaskProcessException(String.format("[%s] execute failed: reason:[%s]",
                    processor.name(), e.getMessage()), e);
        } finally {
            execution.setExecuteEndTime(processor.name());
            execution.getTaskExecution().setEvent(ProcessEvent.class, new ProcessEvent(object));
            listener.afterProcess(execution.getTaskExecution());
        }
        return object;
    }


    /***
     * writer 执行原子
     * @param listeners 监视器
     * @param dataWriter writer
     * @param object 写入数据
     * @param execution 执行器
     * @throws TaskWriterException
     */
    public void writeItem(List<IWriterListener> listeners, IDataWriter dataWriter, List<R> object,
                          TaskExecution execution)
            throws TaskWriterException {
        CompositeWriterListener listener = new CompositeWriterListener();
        if (listeners != null) {
            listener.setListeners(listeners);
        }
//        listener.register(dataWriter.listener());
        execution.setEvent(WriterEvent.class, new WriterEvent(object));
        listener.beforeWrite(execution);
        try {
            execution.setExecuteBeginTime(dataWriter.name(), dataWriter);
            dataWriter.write(execution, object);
            execution.addSuccess(dataWriter);
        } catch (Exception e) {
            execution.addFail(dataWriter);
            execution.setExitStatus(ExitStatus.EXCEPTION);
            listener.onWriteError(execution, e);
            throw new TaskWriterException(String.format("[%s] execute failed: reason:[%s]",
                    dataWriter.name(), e.getMessage()), e);
        } finally {
            execution.setExecuteEndTime(dataWriter.name(), dataWriter);
            listener.afterWrite(execution);
        }
    }

    /***
     * reader执行原子
     * @param dataReader 读数据器
     * @param execution 执行器
     * @return list
     * @throws Exception 读数据异常
     */
    public List<T> readItem(List<IReaderListener> listeners, IDataReader dataReader, TaskExecution execution)
            throws TaskReaderException {
        CompositeReaderListener listener = new CompositeReaderListener();
        if (listeners != null) {
            listener.setListeners(listeners);
        }
//        listener.register(dataReader.listener());
        listener.beforeReader(execution);
        List<T> object = new ArrayList<>();
        try {
            execution.setExecuteBeginTime(dataReader.name(), dataReader);
            object.addAll(dataReader.read(execution));
            execution.addSuccess(dataReader);
        } catch (Exception e) {
            execution.addFail(dataReader);
            execution.setExitStatus(ExitStatus.EXCEPTION);
            listener.onReaderError(execution, e);
            throw new TaskReaderException(String.format("[%s] execute failed: reason:[%s]",
                    dataReader.name(), e.getMessage()), e);
        } finally {
            execution.setExecuteEndTime(dataReader.name(), dataReader);
            execution.setEvent(ReaderEvent.class, new ReaderEvent(object));
            listener.afterReader(execution);
        }
        return object;
    }

}
