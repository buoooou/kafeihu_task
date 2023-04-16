package com.kafeihu.task.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.AnnotationAwareOrderComparator;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.core.annotation.Order;

/**
 * date:  2022/4/20 17:33
 * 顺序集合
 *
 * @author kroulzhang
 */
public class OrderedComposite<S> {

    private List<S> unordered = new ArrayList<>();

    private List<S> ordered = new ArrayList<>();

    private Comparator<? super S> comparator = new AnnotationAwareOrderComparator();

    private List<S> list = new ArrayList<>();

    /**
     * Public setter for the listeners.
     *
     * @param items
     */
    public void setItems(List<? extends S> items) {
        unordered.clear();
        ordered.clear();
        for (S s : items) {
            add(s);
        }
    }

    /**
     * Register additional item.
     *
     * @param item
     */
    public void add(S item) {
        if (item instanceof Ordered) {
            if (!ordered.contains(item)) {
                ordered.add(item);
            }
        } else if (AnnotationUtils.isAnnotationDeclaredLocally(Order.class, item.getClass())) {
            if (!ordered.contains(item)) {
                ordered.add(item);
            }
        } else if (!unordered.contains(item)) {
            unordered.add(item);
        }
        Collections.sort(ordered, comparator);
        list.clear();
        list.addAll(ordered);
        list.addAll(unordered);
    }

    /**
     * Public getter for the list of items. The {@link Ordered} items come
     * first, followed by any unordered ones.
     *
     * @return an iterator over the list of items
     */
    public Iterator<S> iterator() {
        return new ArrayList<>(list).iterator();
    }

    /**
     * Public getter for the list of items in reverse. The {@link Ordered} items
     * come last, after any unordered ones.
     *
     * @return an iterator over the list of items
     */
    public Iterator<S> reverse() {
        ArrayList<S> result = new ArrayList<>(list);
        Collections.reverse(result);
        return result.iterator();
    }

}
