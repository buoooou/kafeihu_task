package com.kafeihu.task.core.event;


/**
 * date:  2022/8/15 19:37
 * process 事件
 *
 * @author kroulzhang
 */
public class ProcessEvent extends AbstractTaskEvent {

    /***
     * 默认构造器
     * @param source
     */
    public ProcessEvent(Object source) {
        super(source);
    }

    @Override
    public String toString() {
        return "ProcessEvent{"
                + "source=" + source
                + '}';
    }
}
