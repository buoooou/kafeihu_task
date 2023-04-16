package com.kafeihu.task.core.exception;

/**
 * date:  2022/4/26 19:10
 * Task任务编排框架异常处理
 *
 * @author kroulzhang
 */
public class TaskProcessException extends RuntimeException {


    /**
     * 构造器
     *
     * @param msg the message
     */
    public TaskProcessException(String msg) {
        super(msg);
    }

    /**
     * 构造器
     *
     * @param msg the message
     * @param cause the cause of the exception
     */
    public TaskProcessException(String msg, Throwable cause) {
        super(msg, cause);
    }
}

