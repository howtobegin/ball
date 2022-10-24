package com.ball.biz.bet.order.settle.analyze;

import com.ball.base.util.BizAssert;
import com.ball.biz.bet.enums.HandicapType;
import com.ball.biz.exception.BizErrCode;
import com.google.common.collect.Maps;

import java.util.Map;

/**
 * @author lhl
 * @date 2022/10/24 下午3:18
 */
public class AnalyzerHolder {
    private static final Map<HandicapType, Analyzer> map = Maps.newConcurrentMap();

    public static void register(HandicapType type, Analyzer impl) {
        map.putIfAbsent(type, impl);
    }

    public static Analyzer get(HandicapType type) {
        Analyzer impl = map.get(type);
        BizAssert.notNull(impl, BizErrCode.NOT_FOUND_IMPL);
        return impl;
    }

    public static Map<HandicapType, Analyzer> all() {
        return map;
    }
}
