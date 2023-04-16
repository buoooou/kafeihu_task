package com.kafeihu.task.core;

import java.util.Map;
import java.util.Map.Entry;
import org.springframework.lang.Nullable;

/**
 * date: 2022/7/15 17:04
 * 上下文容器
 *
 * @author kroulzhang
 */
public class ExecutionContext {

    private AttributeAccessorSupport attribute = new AttributeAccessorSupport();

    public ExecutionContext() {
    }

    /**
     * 默认初始化
     *
     * @param map Initial contents of context.
     */
    public ExecutionContext(Map<String, Object> map) {
        for (Entry<String, Object> entry : map.entrySet()) {
            attribute.setAttribute(entry.getKey(), entry.getValue());
        }
    }

    /***
     * 添加值
     * @param key key
     * @param value 值
     */
    public void putString(String key, @Nullable String value) {
        attribute.setAttribute(key, value);
    }

    /***
     * 获取值
     * @param key key
     * @return
     */
    public String getString(String key) {

        return (String) attribute.getAttribute(key);
    }

    /***
     * 获取值
     * @param key key
     * @param defaultString 默认值
     * @return
     */
    public String getString(String key, String defaultString) {
        if (!attribute.hasAttribute(key)) {
            return defaultString;
        }

        return getString(key);
    }

    /***
     * 删除
     * @param key key
     * @return
     */
    public Object remove(String key) {
        return attribute.removeAttribute(key);
    }

    /***
     * 清空
     */
    public void clear() {
        attribute.clearAttribute();
    }

    /***
     * 获取值
     * @param key key
     * @return
     */
    public Object get(String key) {
        return attribute.getAttribute(key);
    }

    /***
     * 获取值
     * @param key key
     * @param defaultObject 默认值
     * @return
     */
    public Object get(String key, Object defaultObject) {

        if (!attribute.hasAttribute(key)) {
            return defaultObject;
        }

        return get(key);
    }

    /***
     * 添加值
     * @param key key
     * @param value 值
     */
    public void put(String key, @Nullable Object value) {
        attribute.setAttribute(key, value);
    }


    /***
     * 是否包括
     * @param key key
     * @return
     */
    public boolean contains(String key) {
        return attribute.hasAttribute(key);
    }

    @Override
    public String toString() {
        return "ExecutionContext{"
                + "attribute=" + attribute
                + '}';
    }
}
