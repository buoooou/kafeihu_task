package com.kafeihu.task.core.exception;

/**
 * date:  2022/4/26 19:10
 * Task任务编排框架异常处理
 *
 * @author kroulzhang
 */
public class TaskReaderException extends RuntimeException {


    /**
     * 构造器
     *
     * @param msg the message
     */
    public TaskReaderException(String msg) {
        super(msg);
    }

    /**
     * 构造器
     *
     * @param msg the message
     * @param cause the cause of the exception
     */
    public TaskReaderException(String msg, Throwable cause) {
        super(msg, cause);
    }
}

