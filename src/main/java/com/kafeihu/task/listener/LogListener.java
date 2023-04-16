package com.kafeihu.task.listener;

import com.kafeihu.task.core.TaskExecution;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * date: 2022/6/13 15:26
 * log listener
 *
 * @author kroulzhang
 */
public class LogListener implements IListener {

    private static final Logger logger = LoggerFactory.getLogger(LogListener.class);
    private static String className;
    private static String methodName;


    public LogListener(String className, String methodName) {
        this.className = className;
        this.methodName = methodName;
    }


    @Override
    public void beforeExecute(TaskExecution execution) {
        logger.info("Task:[{}] {}.{} execute begin", execution.getCommandParameters(), className, methodName);
    }

    @Override
    public void afterExecute(TaskExecution execution) {
        logger.info("Task:[{}] {}.{} execute end", execution.getCommandParameters(), className, methodName);
    }

    @Override
    public void onExecuteError(TaskExecution execution, Exception e) {
        logger.error("Task:[{}] {}.{} execute error:{}",
                execution.getCommandParameters(), methodName, className, e);
    }
}
