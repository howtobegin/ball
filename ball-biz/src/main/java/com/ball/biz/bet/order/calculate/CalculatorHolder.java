package com.ball.biz.bet.order.calculate;

import com.ball.base.util.BizAssert;
import com.ball.biz.bet.enums.BetResult;
import com.ball.biz.exception.BizErrCode;
import com.google.common.collect.Maps;

import java.util.Map;

/**
 * @author scliuhailin@163.com
 * @date 2022/8/13 上午11:39
 */
public class CalculatorHolder {

    private static Map<BetResult, Calculator> map = Maps.newHashMap();

    public static void register(BetResult type, Calculator impl) {
        map.putIfAbsent(type, impl);
    }

    public static Calculator get(BetResult type) {
        Calculator impl = map.get(type);
        BizAssert.notNull(impl, BizErrCode.NOT_FOUND_IMPL);
        return impl;
    }
}
