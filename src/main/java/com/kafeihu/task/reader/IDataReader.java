package com.kafeihu.task.reader;

import com.kafeihu.task.listener.IListener;
import com.kafeihu.task.listener.LogListener;
import com.kafeihu.task.core.IStep;
import com.kafeihu.task.core.TaskExecution;

import java.util.List;


/**
 * date: 2022/3/25 15:57
 * 数据读取器
 *
 * @param <T> T 读数据结构体
 * @author kroulzhang
 */
public interface IDataReader<T> extends IStep<List<T>, Object> {

//    /***
//     * 获取分页，每页数量
//     * @return
//     */
//    int getPageNum();
//
//    /***
//     * 获取分页起始num
//     * @return
//     */
//    int getStartPageNum();

    /***
     * 批量读取数据
     * @param execution
     * @return
     */
    List<T> read(TaskExecution execution) throws Exception;

    @Override
    default List<T> run(Object object, TaskExecution taskExecution) throws Exception {
        return read(taskExecution);
    }

    /***
     * 默认监听器
     * @return
     */
    @Override
    default IListener listener() {
        return new LogListener(this.getClass().getSimpleName(), "read");
    }

}
