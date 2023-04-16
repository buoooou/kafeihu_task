package com.kafeihu.task.core.event;

/**
 * date:  2022/8/15 19:37
 * writer 事件
 *
 * @author kroulzhang
 */
public class WriterEvent extends AbstractTaskEvent {

    /***
     * 默认构造器
     * @param source
     */
    public WriterEvent(Object source) {
        super(source);
    }

    @Override
    public String toString() {
        return "WriterEvent{"
                + "source=" + source
                + '}';
    }
}
