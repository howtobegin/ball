package com.ball.base.util;

import org.springframework.beans.BeanUtils;

public class BeanUtil {
    public static <T> T copy(Object source, Class<T> target) {
        return copy(source, target, false);
    }

    public static <T> T copy(Object source, Class<T> target, boolean returnNullIfSourceNull) {
        if (returnNullIfSourceNull && source == null) {
            return null;
        }
        try {
            T t = target.newInstance();
            if (source != null) {
                BeanUtils.copyProperties(source, t);
            }
            return t;
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }
    }
}
