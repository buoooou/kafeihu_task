package com.kafeihu.task.core.event;


/**
 * date:  2022/8/15 19:32
 * task事件
 *
 * @author kroulzhang
 */
public abstract class AbstractTaskEvent {

    protected transient Object source;

    /***
     * 默认构造器
     * @param source 数据源
     */
    public AbstractTaskEvent(Object source) {
        if (source == null) {
            throw new IllegalArgumentException("null source");
        }

        this.source = source;
    }

    /***
     * 获取数据源
     * @return 数据源
     */
    public Object getSource() {
        return source;
    }

    @Override
    public String toString() {
        return "TaskEvent{"
                + "source=" + source
                + '}';
    }
}
