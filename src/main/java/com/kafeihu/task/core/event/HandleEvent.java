package com.kafeihu.task.core.event;


/**
 * date:  2022/8/15 19:37
 * handle 事件
 *
 * @author kroulzhang
 */
@Deprecated
public class HandleEvent extends AbstractTaskEvent {

    /***
     * 默认构造器
     * @param source 数据源
     */
    public HandleEvent(Object source) {
        super(source);
    }

    @Override
    public String toString() {
        return "HandleEvent{"
                + "source=" + source
                + '}';
    }
}
