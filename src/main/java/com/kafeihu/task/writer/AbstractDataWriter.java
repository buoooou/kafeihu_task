package com.kafeihu.task.writer;

import com.kafeihu.task.core.TaskExecution;
import com.kafeihu.task.core.exception.TaskWriterException;
import java.util.List;

/**
 * date: 2022/10/17 18:54
 *
 * @author kroulzhang
 */
public abstract class AbstractDataWriter implements IDataWriter {

    @Override
    public void write(TaskExecution execution, List object) throws TaskWriterException {

    }
    

}
