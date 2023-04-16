package com.kafeihu.task.launch;

import com.kafeihu.task.core.AttributeAccessorSupport;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentSkipListSet;
import org.springframework.lang.Nullable;

/**
 * date: 2022/4/28 16:10
 * 任务执行参数
 *
 * @author kroulzhang
 */
public class TaskCommandParameters {

    //目前暂时无用，
    private final Set<String> options;
    private AttributeAccessorSupport params = new AttributeAccessorSupport();

    public TaskCommandParameters() {
        this(new ConcurrentHashMap<>());
    }

    public TaskCommandParameters(Map<String, String> attribute) {
        Iterator<String> iterator = attribute.keySet().iterator();
        while (iterator.hasNext()) {
            String s = iterator.next();
            params.setAttribute(s, attribute.get(s));
        }
        this.options = new ConcurrentSkipListSet<>();
    }

    public TaskCommandParameters(Map<String, String> attribute, Set<String> options) {
        Iterator<String> iterator = attribute.keySet().iterator();
        while (iterator.hasNext()) {
            String s = iterator.next();
            params.setAttribute(s, attribute.get(s));
        }
        this.options = options;
    }

    @Nullable
    public String getParameter(String key) {
        String value = (String) params.getAttribute(key);
        return value == null ? null : value;
    }

    @Nullable
    public String getParameter(String key, @Nullable String defaultValue) {
        if (params.hasAttribute(key)) {
            return getParameter(key);
        } else {
            return defaultValue;
        }
    }


    @Override
    public String toString() {
        return "TaskCommandParameters{"
                + ", options=" + options
                + ", params=" + params
                + '}';
    }

    public Map<String, Object> getParameters() {
        return new LinkedHashMap<>(params.getAttributes());
    }


}
