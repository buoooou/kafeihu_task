package com.kafeihu.task.writer;

import com.kafeihu.task.listener.IListener;
import com.kafeihu.task.listener.LogListener;
import com.kafeihu.task.core.IStep;
import com.kafeihu.task.core.TaskExecution;

import java.util.List;

/**
 * date:  2022/4/17 20:50
 * 数据写入
 *
 * @param <T> 读取数据源类型
 * @author kroulzhang
 */
public interface IDataWriter<T> extends IStep<Object, List<T>> {

    /***
     * 批量写数据
     * @param execution
     * @param object
     */
    void write(TaskExecution execution, List<T> object) throws Exception;

    @Override
    default Object run(List<T> object, TaskExecution taskExecution) throws Exception {
        write(taskExecution, object);
        return new Object();
    }

    /***
     * 默认监听器
     * @return
     */
    @Override
    default IListener listener() {
        return new LogListener(this.getClass().getSimpleName(), "write");
    }

}
