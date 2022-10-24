package com.ball.biz.bet.processor;

import com.ball.base.util.BizAssert;
import com.ball.biz.bet.enums.HandicapType;
import com.ball.biz.exception.BizErrCode;
import com.google.common.collect.Maps;

import java.util.Map;

/**
 * @author lhl
 * @date 2022/10/22 上午11:56
 */
public class BetProcessorHolder {
    private static final Map<HandicapType, BetProcessor> map = Maps.newHashMap();

    public static void register(HandicapType type, BetProcessor impl) {
        map.putIfAbsent(type, impl);
    }

    public static BetProcessor get(HandicapType type) {
        BetProcessor impl = map.get(type);
        BizAssert.notNull(impl, BizErrCode.NOT_FOUND_IMPL);
        return impl;
    }
}
