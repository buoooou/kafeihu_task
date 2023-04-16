package com.kafeihu.task.reader;

import com.kafeihu.task.core.TaskExecution;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * date: 2022/3/31 17:29
 * 抽象数据读取器
 *
 * @param <T> 读取数据源类型
 * @author kroulzhang
 */
public abstract class AbstractDataReader<T> implements IDataReader {

    protected final Logger logger = LoggerFactory.getLogger(this.getClass());

//    private int pageNum;
//
//    private int startPageNum;
//
//    @Override
//    public int getPageNum() {
//        return pageNum;
//    }
//
//    public void setPageNum(int pageNum) {
//        this.pageNum = pageNum;
//    }
//
//    @Override
//    public int getStartPageNum() {
//        return startPageNum;
//    }
//
//    public void setStartPageNum(int startPageNum) {
//        this.startPageNum = startPageNum;
//    }

    /***
     * 设置参数等
     */
    public abstract void beforeRead(TaskExecution execution);

    /***
     * 读取数据
     * @return
     */
    public abstract List<T> doRead(TaskExecution execution) throws Exception;


    @Override
    public List<T> read(TaskExecution execution) throws Exception {
        logger.info("Task:[{}] read data begin", execution.getCommandParameters());
        beforeRead(execution);
        return doRead(execution);
    }


}
