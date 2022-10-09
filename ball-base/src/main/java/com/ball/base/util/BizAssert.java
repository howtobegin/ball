package com.ball.base.util;


import com.ball.base.exception.AssertException;
import com.ball.base.exception.IBizErrCode;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.util.Collection;

/**
 * @author littlehow
 */
public class BizAssert {
    public static void isTrue(boolean flag, IBizErrCode message, String... obj) {
        if (!flag) {
            throw new AssertException(message, obj);
        }
    }

    public static void notNull(Object obj, IBizErrCode message, String... argus) {
        if (obj == null) {
            throw new AssertException(message, argus);
        }
    }

    public static void isNull(Object obj, IBizErrCode message, String... argus) {
        if (obj != null) {
            throw new AssertException(message, argus);
        }
    }

    public static void isZero(int data, IBizErrCode message, String... argus) {
        if (data != 0) {
            throw new AssertException(message, argus);
        }
    }

    public static void isZero(BigDecimal value, IBizErrCode message, String... argus) {
        if (value == null || value.compareTo(BigDecimal.ZERO) != 0) {
            throw new AssertException(message, argus);
        }
    }

    public static void notEmpty(Collection collection, IBizErrCode message, String... argus) {
        if (CollectionUtils.isEmpty(collection)) {
            throw new AssertException(message, argus);
        }
    }

    public static void isEmpty(Collection collection, IBizErrCode message, String... argus) {
        if (!CollectionUtils.isEmpty(collection)) {
            throw new AssertException(message, argus);
        }
    }

    public static void hasText(String value, IBizErrCode message, String... argus) {
        if (!StringUtils.hasText(value)) {
            throw new AssertException(message, argus);
        }
    }

    public static void hasAmount(BigDecimal value, IBizErrCode message, String... argus) {
        if (value == null || value.compareTo(BigDecimal.ZERO) <= 0) {
            throw new AssertException(message, argus);
        }
    }
}
