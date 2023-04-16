package com.kafeihu.task.core;

import com.kafeihu.task.listener.IListener;

/**
 * date:  2022/8/5 22:49
 * step：无行为能力，只是定义规范
 *
 * @param <R> 写数据结构体
 * @param <T> 读数据结构体
 * @author kroulzhang
 */
public interface IStep<R, T> {

    /***
     * 处理器名称
     * @return
     */
    default String name() {
        return this.getClass().getSimpleName();
    }

    R run(T object, TaskExecution taskExecution) throws Exception;

    IListener listener();
}
