package com.kafeihu.task.reader;

import com.kafeihu.task.listener.IListener;
import com.kafeihu.task.listener.LogListener;
import com.kafeihu.task.core.IStep;
import com.kafeihu.task.core.TaskExecution;

import java.util.List;

/**
 * date:  2022/8/12 21:57
 * 多数据源Reader
 *
 * @param <T> 读数据结构体
 * @author kroulzhang
 */
public interface IMultiDataReader<T> extends IStep<List<IDataReader<T>>, Object> {

    /***
     * 构造多输入源
     * @return
     */
    List<IDataReader<T>> getAllDataReader() throws Exception;

    @Override
    default List<IDataReader<T>> run(Object object, TaskExecution taskExecution) throws Exception {
        return getAllDataReader();
    }

    /***
     * 默认监听器
     * @return
     */
    @Override
    default IListener listener() {
        return new LogListener(this.getClass().getSimpleName(), "getAllDataReader");
    }
}
