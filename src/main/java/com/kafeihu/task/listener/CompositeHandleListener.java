package com.kafeihu.task.listener;

import com.kafeihu.task.core.TaskExecution;
import com.kafeihu.task.util.OrderedComposite;
import java.util.Iterator;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * date:  2022/4/20 17:30
 * 组合listener
 *
 * @author kroulzhang
 */
public class CompositeHandleListener implements IHandleListener {

    private OrderedComposite<IHandleListener> list = new OrderedComposite<>();
    private static final Logger logger = LoggerFactory.getLogger(LogListener.class);

    public void setListeners(List<IHandleListener> listeners) {
        list.setItems(listeners);
    }


    public void register(IHandleListener listener) {
        list.add(listener);
    }


    @Override
    public void beforeHandle(TaskExecution execution) {
        for (Iterator<IHandleListener> iterator = list.reverse(); iterator.hasNext(); ) {
            IHandleListener listener = iterator.next();
            listener.beforeHandle(execution);
        }
    }

    @Override
    public void onHandleError(TaskExecution execution, Exception e) {
        for (Iterator<IHandleListener> iterator = list.reverse(); iterator.hasNext(); ) {
            IHandleListener listener = iterator.next();
            listener.onHandleError(execution, e);
        }
    }

    @Override
    public void afterHandle(TaskExecution execution) {
        for (Iterator<IHandleListener> iterator = list.iterator(); iterator.hasNext(); ) {
            IHandleListener listener = iterator.next();
            listener.afterHandle(execution);
        }
    }

    @Override
    public String toString() {
        return "CompositeListener{"
                + "list=" + list
                + '}';
    }
}
