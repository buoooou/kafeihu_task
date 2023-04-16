package com.kafeihu.task.listener;

import com.kafeihu.task.core.TaskExecution;
import com.kafeihu.task.util.OrderedComposite;
import java.util.Iterator;
import java.util.List;

/**
 * date:  2022/4/20 17:30
 * 组合listener
 *
 * @author kroulzhang
 */
public class CompositeListener implements IListener {

    private OrderedComposite<IListener> list = new OrderedComposite<>();


    public void setListeners(List<? extends IListener> listeners) {
        list.setItems(listeners);
    }


    public void register(IListener listener) {
        list.add(listener);
    }


    @Override
    public void afterExecute(TaskExecution execution) {
        for (Iterator<IListener> iterator = list.reverse(); iterator.hasNext(); ) {
            IListener listener = iterator.next();
            listener.afterExecute(execution);
        }
    }

    @Override
    public void onExecuteError(TaskExecution execution, Exception e) {
        for (Iterator<IListener> iterator = list.reverse(); iterator.hasNext(); ) {
            IListener listener = iterator.next();
            listener.onExecuteError(execution, e);
        }
    }

    @Override
    public void beforeExecute(TaskExecution execution) {
        for (Iterator<IListener> iterator = list.iterator(); iterator.hasNext(); ) {
            IListener listener = iterator.next();
            listener.beforeExecute(execution);
        }
    }

    @Override
    public String toString() {
        return "CompositeListener{"
                + "list=" + list
                + '}';
    }
}
