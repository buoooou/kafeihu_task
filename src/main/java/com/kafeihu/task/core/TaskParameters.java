package com.kafeihu.task.core;

import com.kafeihu.task.launch.TaskCommandParameters;

import java.time.LocalDateTime;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Properties;
import org.springframework.lang.Nullable;

/**
 * date: 2022/8/24 14:44
 * Task参数
 *
 * @author kroulzhang
 */
public class TaskParameters {

    private final AttributeAccessorSupport params;
    private final Map<String, String> commondParams = new LinkedHashMap<>();

    public TaskParameters() {
        this.params = new AttributeAccessorSupport();
    }

    public TaskParameters(Map<String, Object> attribute) {
        this.params = new AttributeAccessorSupport();
        Iterator<String> iterator = attribute.keySet().iterator();
        while (iterator.hasNext()) {
            String s = iterator.next();
            this.params.setAttribute(s, attribute.get(s));
        }
    }

    @Nullable
    public String getString(String key) {
        String value = (String) params.getAttribute(key);
        return value == null ? null : value;
    }

    @Nullable
    public String getString(String key, @Nullable String defaultValue) {
        if (params.hasAttribute(key)) {
            return getString(key);
        } else {
            return defaultValue;
        }
    }

    @Nullable
    public Long getLong(String key) {
        if (!params.hasAttribute(key)) {
            return null;
        }
        Object value = params.getAttribute(key);
        return value == null ? null : ((Long) value).longValue();
    }

    @Nullable
    public Long getLong(String key, @Nullable Long defaultValue) {
        if (params.hasAttribute(key)) {
            return getLong(key);
        } else {
            return defaultValue;
        }
    }

    @Nullable
    public Double getDouble(String key) {
        if (!params.hasAttribute(key)) {
            return null;
        }
        Double value = (Double) params.getAttribute(key);
        return value == null ? null : value.doubleValue();
    }

    @Nullable
    public Double getDouble(String key, @Nullable Double defaultValue) {
        if (params.hasAttribute(key)) {
            return getDouble(key);
        } else {
            return defaultValue;
        }
    }

    @Nullable
    public LocalDateTime getLocalDateTime(String key) {
        if (!params.hasAttribute(key)) {
            return null;
        }
        LocalDateTime value = (LocalDateTime) params.getAttribute(key);
        return value == null ? null : value;
    }

    @Nullable
    public LocalDateTime getLocalDateTime(String key, @Nullable LocalDateTime defaultValue) {
        if (params.hasAttribute(key)) {
            return getLocalDateTime(key);
        } else {
            return defaultValue;
        }
    }

    public boolean isEmpty() {
        return params.isEmpty();
    }

    public Map<String, Object> getParameters() {
        return new LinkedHashMap<>(params.getAttributes());
    }

    @Override
    public String toString() {
        return "TaskParameters{"
                + "params=" + params
                + '}';
    }

    public Properties toProperties() {
        return params.toProperties();
    }

    public void setTaskCommondParameters(Map<String, Object> attribute) {
        Iterator<String> iterator = attribute.keySet().iterator();
        while (iterator.hasNext()) {
            String s = iterator.next();
            this.commondParams.put(s, (String) attribute.get(s));
        }
    }

    public TaskCommandParameters getTaskCommondParameters() {
        return new TaskCommandParameters(commondParams);
    }
}
