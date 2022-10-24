package com.ball.biz.bet.order.settle.parse;

import com.ball.base.util.BizAssert;
import com.ball.biz.bet.enums.BetType;
import com.ball.biz.bet.enums.HandicapType;
import com.ball.biz.exception.BizErrCode;
import com.google.common.collect.Maps;

import java.util.Map;

/**
 * @author lhl
 * @date 2022/10/23 上午9:02
 */
public class ParserHolder {
    private static final Map<HandicapType, Map<BetType, Parser>> map = Maps.newConcurrentMap();

    public static void register(HandicapType type, BetType betType, Parser impl) {
        Map<BetType, Parser> implMap = map.get(type);
        if (implMap == null) {
            implMap = Maps.newHashMap();
            map.put(type, implMap);
        }
        implMap.putIfAbsent(betType, impl);
    }

    public static Parser get(HandicapType type, BetType betType) {
        Parser impl = map.getOrDefault(type, Maps.newHashMap()).getOrDefault(betType, null);
        BizAssert.notNull(impl, BizErrCode.NOT_FOUND_IMPL);
        return impl;
    }

    public static Map<HandicapType, Map<BetType, Parser>> all() {
        return map;
    }
}
