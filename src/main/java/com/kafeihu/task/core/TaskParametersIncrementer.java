package com.kafeihu.task.core;

/**
 * date: 2022/8/24 17:04
 * Task参数增量器
 *
 * @author kroulzhang
 */
public interface TaskParametersIncrementer {

    TaskParameters getNext();
}
