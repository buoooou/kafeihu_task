package com.kafeihu.task.listener;

import com.kafeihu.task.core.TaskExecution;
import com.kafeihu.task.util.OrderedComposite;
import java.util.Iterator;
import java.util.List;

/**
 * date: 2022/4/20 17:30
 * 组合listener
 *
 * @author kroulzhang
 */
public class CompositeWriterListener implements IWriterListener {

    private OrderedComposite<IWriterListener> list = new OrderedComposite<>();


    public void setListeners(List<IWriterListener> listeners) {
        list.setItems(listeners);
    }


    public void register(IWriterListener listener) {
        list.add(listener);
    }


    @Override
    public void beforeWrite(TaskExecution execution) {
        for (Iterator<IWriterListener> iterator = list.reverse(); iterator.hasNext(); ) {
            IWriterListener listener = iterator.next();
            listener.beforeWrite(execution);
        }
    }

    @Override
    public void onWriteError(TaskExecution execution, Exception e) {
        for (Iterator<IWriterListener> iterator = list.reverse(); iterator.hasNext(); ) {
            IWriterListener listener = iterator.next();
            listener.onWriteError(execution, e);
        }
    }

    @Override
    public void afterWrite(TaskExecution execution) {
        for (Iterator<IWriterListener> iterator = list.iterator(); iterator.hasNext(); ) {
            IWriterListener listener = iterator.next();
            listener.afterWrite(execution);
        }
    }

    @Override
    public String toString() {
        return "CompositeListener{"
                + "list=" + list
                + '}';
    }
}
