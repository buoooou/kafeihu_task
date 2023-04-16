package com.kafeihu.task.process;

import com.kafeihu.task.core.ITaskExecutor;
import com.kafeihu.task.listener.IListener;
import com.kafeihu.task.listener.IProcessListener;
import com.kafeihu.task.listener.IReaderListener;
import com.kafeihu.task.listener.IWriterListener;
import com.kafeihu.task.core.IStep;
import com.kafeihu.task.core.TaskExecution;
import com.kafeihu.task.core.exception.TaskProcessException;
import com.kafeihu.task.reader.IDataReader;
import com.kafeihu.task.reader.IMultiDataReader;
import com.kafeihu.task.writer.IDataWriter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.lang.NonNull;

/***
 * date:  2022/4/17 20:50
 * 默认process模式处理器
 *
 * @param <R> 写数据结构体
 * @param <T> 读数据结构体
 *
 * @author kroulzhang
 */
public class DefaultProcessTaskExecutor<R, T> implements ITaskExecutor<R, T> {

    protected final Logger logger = LoggerFactory.getLogger(this.getClass());
    private List<IProcessListener> processListeners = new CopyOnWriteArrayList<>();
    private List<IWriterListener> writerListeners = new CopyOnWriteArrayList<>();
    private List<IReaderListener> readerListeners = new CopyOnWriteArrayList<>();

    private TaskExecutorHelper helper = new TaskExecutorHelper();
    //默认逐条处理
    private List<IDataReader<? extends T>> dataReaders = new CopyOnWriteArrayList<>();
    private List<IMultiDataReader> multiDataReaders = new CopyOnWriteArrayList<>();
    private IDataWriter<? super R> dataWriter;
    /***
     * 整合ICleanDataConvertor
     * CleanFeeCalculator
     * IPayoffDataConvertor为
     * process
     */
    private List<IProcess<? super R, ? extends T>> processList = new CopyOnWriteArrayList<>();
    //全局预处理process
    private IProcess<? super R, ? extends T> preProcess;
    private boolean skipFault = false;

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

    @Override
    public void setSkipFault(boolean skipFault) {
        this.skipFault = skipFault;
    }

    public boolean skipFault() {
        return this.skipFault;
    }

    /***
     * process 任务处理模式
     * 暂时只支持模式：批量读取数据--单条数据经过多个process处理--批量写入数据
     * 1.读取数据
     * 2.处理数据 R, T（可支持无需处理数据，直接写入，需满足T extends of R）
     * 3.写入数据
     * @throws Exception
     */
    @Override
    public void execute(TaskExecution execution) throws Exception {
        execution.setSum(1, dataWriter);
        //多数据源支持
        //每个数据源的数据分开处理，分开写入
        for (IMultiDataReader multiDataReader : multiDataReaders) {
            dataReaders.addAll(multiDataReader.getAllDataReader());
        }
        Iterator<IDataReader<? extends T>> iterator = dataReaders.iterator();
        while (iterator.hasNext()) {
            try {
                //读数据处理
                IDataReader<? extends T> dataReader = iterator.next();
                execution.setSum(dataReaders.size(), dataReader);
                List<T> datas = helper.readItem(readerListeners, dataReader, execution);
                if (datas.size() <= 0) {
                    logger.warn("Task:[{}] data Empty", execution.getTaskParameters());
                }

                //处理数据
                List<R> result = executeProcess(datas, execution);

                //写入数据
                helper.writeItem(writerListeners, dataWriter, result, execution);

            } catch (Exception e) {
                if (skipFault()) {
                    logger.warn(String.format("process execute failed: reason:[%s]", e.getMessage()), e);
                    continue;
                } else {
                    throw e;
                }
            }
        }
    }

    @Override
    public void addStep(Class clazz, @NonNull IStep step) {

        if (clazz.isAssignableFrom(Deprecated.class)) {
            //TODO 特殊处理
            preProcess = (IProcess<R, T>) step;
        } else if (step instanceof IDataReader) {
            dataReaders.add((IDataReader<T>) step);
        } else if (step instanceof IMultiDataReader) {
            IMultiDataReader<T> reader = (IMultiDataReader<T>) step;
            multiDataReaders.add(reader);
        } else if (step instanceof IDataWriter) {
            dataWriter = (IDataWriter<R>) step;
        } else if (step instanceof IProcess) {
            processList.add((IProcess<R, T>) step);
        }

    }

    /***
     * 预先数据处理，包括处理模式：单条处理
     * preProcess 限定该处理器只能处理一批数据，并对一批数据做修改
     * @param datas
     * @param execution
     * @return
     */
    private List<R> executeProcess(List<T> datas, TaskExecution execution) throws TaskProcessException {
        List<R> result = new ArrayList<>();
        ProcessExecution processExecution = execution.getProcessExecution();
        //todo 移除preProcess统计
        processExecution.setSum(processList.size() + 1);
        //todo 废弃
        if (preProcess != null) {

            datas = (List<T>) helper.processItem(processListeners, preProcess, datas,
                    processExecution);
        }

        //可支持无需处理数据，直接写入，需满足T instance of R
        if (processList.size() == 0) {

            logger.info("Task:[{}] with no process write data success", execution.getCommandParameters());
            return (List<R>) datas;
        }

        Iterator<T> iterator = datas.iterator();

        while (iterator.hasNext()) {
            T data = iterator.next();
            R res = process(data, processExecution);
            if (res instanceof Collection) {
                result.addAll((Collection<? extends R>) res);
            } else {
                result.add(res);
            }
            processExecution.getExecutionContext().clear();
        }
        return result;
    }

    /***
     * 根据 预设processor 处理数据
     * processors处理数据类型转换
     * 第一个process R, T 入参T  出参R
     * 第二个process R, T 入参T  出参R   (需满足第一个process.R==第二个process.T)
     * 第三个process R, T 入参T  出参R   (需满足第二个process.R==第三个process.T)
     * 、、、
     * 第N个process R, T 入参T  出参R   (需满足第N-1个process.R==第N个process.T)
     *
     * UniDependentTask R, T   从最初的T经过N个process后转换为R，process期间类型转换process自己控制
     *     ======UniCleanTask.T ==第一个process.T
     *     ======UniCleanTask.R ==第N个process.R
     *
     * @param item 入参
     * @param execution 执行器
     * @return R 出参
     * @throws TaskProcessException
     */
    private R process(T item, ProcessExecution execution) throws TaskProcessException {
        Object result = item;
        for (IProcess<? super R, ? extends T> delegate : processList) {
            try {

                if (result == null) {
                    return null;
                }
                result = helper.processItem(processListeners, delegate, result,
                        execution);

            } catch (Exception e) {
                throw new TaskProcessException(String.format("process:[%s] execute failed: reason:[%s]",
                        delegate.name(), e.getMessage()), e);
            }
        }
        return (R) result;
    }

}
