package com.kafeihu.task.core;

import org.springframework.lang.Nullable;

/**
 * date:  2022/5/28 21:01
 * 参数生成
 *
 * @author kroulzhang
 */
public interface AttributeAccessor {

    /***
     * 设置值
     * @param name name
     * @param value 值
     */
    void setAttribute(String name, @Nullable Object value);

    /**
     * 获取值
     *
     * @param name name
     * @return 值
     */
    @Nullable
    Object getAttribute(String name);

    /**
     * 删除值
     *
     * @param name name
     * @return 值
     */
    @Nullable
    Object removeAttribute(String name);

    /***
     * 清空数据
     */
    void clearAttribute();

    /**
     * 是否包括
     *
     * @param name name
     */
    boolean hasAttribute(String name);
}
