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
public class CompositeReaderListener implements IReaderListener {

    private OrderedComposite<IReaderListener> list = new OrderedComposite<>();


    public void setListeners(List<IReaderListener> listeners) {
        list.setItems(listeners);
    }


    public void register(IReaderListener listener) {
        list.add(listener);
    }


    @Override
    public void beforeReader(TaskExecution execution) {
        for (Iterator<IReaderListener> iterator = list.reverse(); iterator.hasNext(); ) {
            IReaderListener listener = iterator.next();
            listener.beforeReader(execution);
        }
    }

    @Override
    public void onReaderError(TaskExecution execution, Exception e) {
        for (Iterator<IReaderListener> iterator = list.reverse(); iterator.hasNext(); ) {
            IReaderListener listener = iterator.next();
            listener.onReaderError(execution, e);
        }
    }

    @Override
    public void afterReader(TaskExecution execution) {
        for (Iterator<IReaderListener> iterator = list.iterator(); iterator.hasNext(); ) {
            IReaderListener listener = iterator.next();
            listener.afterReader(execution);
        }
    }

    @Override
    public String toString() {
        return "CompositeListener{"
                + "list=" + list
                + '}';
    }
}
