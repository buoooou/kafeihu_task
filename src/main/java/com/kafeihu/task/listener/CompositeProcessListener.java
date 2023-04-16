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
public class CompositeProcessListener implements IProcessListener {

    private OrderedComposite<IProcessListener> list = new OrderedComposite<>();
    private static final Logger logger = LoggerFactory.getLogger(LogListener.class);

    public void setListeners(List<IProcessListener> listeners) {
        list.setItems(listeners);
    }


    public void register(IProcessListener listener) {
        list.add(listener);
    }


    @Override
    public void beforeProcess(TaskExecution execution) {
        for (Iterator<IProcessListener> iterator = list.reverse(); iterator.hasNext(); ) {
            IProcessListener listener = iterator.next();
            listener.beforeProcess(execution);
        }
    }

    @Override
    public void onProcessError(TaskExecution execution, Exception e) {
        for (Iterator<IProcessListener> iterator = list.reverse(); iterator.hasNext(); ) {
            IProcessListener listener = iterator.next();
            listener.onProcessError(execution, e);
        }
    }

    @Override
    public void afterProcess(TaskExecution execution) {
        for (Iterator<IProcessListener> iterator = list.iterator(); iterator.hasNext(); ) {
            IProcessListener listener = iterator.next();
            listener.afterProcess(execution);
        }
    }

    @Override
    public String toString() {
        return "CompositeListener{"
                + "list=" + list
                + '}';
    }
}
