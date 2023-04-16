package com.kafeihu.task.reader;

import java.util.List;

/***
 *
 * date: 2022/7/15 14:40
 *  通用数据查询函数
 *
 * @param <T> 写数据结构体
 * @param <R> 读数据结构体
 *
 *  @author kroulzhang
 */
@FunctionalInterface
public interface DataQueryFunction<T, R> {

    List<R> query(T params) throws Exception;
}
