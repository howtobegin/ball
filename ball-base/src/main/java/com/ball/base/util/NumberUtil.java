package com.ball.base.util;

import org.springframework.util.StringUtils;

/**
 * @author JimChery
 */
public abstract class NumberUtil {
    public static boolean isDigital(String v) {
        return StringUtils.hasText(v) && v.replaceAll("\\d", "").length() == 0;
    }
}
