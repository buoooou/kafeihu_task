package com.kafeihu.task.core;

import java.io.Serializable;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Properties;
import org.springframework.lang.Nullable;
import org.springframework.util.Assert;


/**
 * date:  2022/5/28 21:01
 * 参数默认支撑
 *
 * @author kroulzhang
 */
public class AttributeAccessorSupport implements AttributeAccessor, Serializable {


    private final Map<String, Object> attributes = new LinkedHashMap<>();

    @Override
    public void setAttribute(String name, @Nullable Object value) {
        Assert.notNull(name, "Name must not be null");
        if (value != null) {
            this.attributes.put(name, value);
        } else {
            removeAttribute(name);
        }
    }

    @Override
    @Nullable
    public Object getAttribute(String name) {
        Assert.notNull(name, "Name must not be null");
        return this.attributes.get(name);
    }

    @Override
    @Nullable
    public Object removeAttribute(String name) {
        Assert.notNull(name, "Name must not be null");
        return this.attributes.remove(name);
    }

    @Override
    public boolean hasAttribute(String name) {
        Assert.notNull(name, "Name must not be null");
        return this.attributes.containsKey(name);
    }

    @Override
    public void clearAttribute() {
        this.attributes.clear();
    }

    @Override
    public boolean equals(@Nullable Object other) {
        return (this == other || (other instanceof AttributeAccessorSupport
                && this.attributes.equals(((AttributeAccessorSupport) other).attributes)));
    }

    public boolean isEmpty() {
        return attributes.isEmpty();
    }

    @Override
    public int hashCode() {
        return this.attributes.hashCode();
    }

    @Override
    public String toString() {
        return "AttributeAccessorSupport{"
                + "attributes=" + attributes
                + '}';
    }

    public Map<String, Object> getAttributes() {
        return new LinkedHashMap<>(attributes);
    }

    public Properties toProperties() {
        Properties props = new Properties();

        for (Map.Entry<String, Object> param : attributes.entrySet()) {
            if (param.getValue() != null) {
                props.put(param.getKey(), param.getValue().toString());
            }
        }

        return props;
    }
}
