package com.kafeihu.task.core.event;


/**
 * date:  2022/8/15 19:37
 * reader 事件
 *
 * @author kroulzhang
 */
public class ReaderEvent extends AbstractTaskEvent {

    /***
     * 默认构造器
     * @param source
     */
    public ReaderEvent(Object source) {
        super(source);
    }

    @Override
    public String toString() {
        return "ReaderEvent{"
                + "source=" + source
                + '}';
    }
}
