package com.ball.biz.bet.order.settle.assist;

import com.alibaba.fastjson.JSON;
import com.ball.base.util.BizAssert;
import com.ball.biz.bet.enums.BetType;
import com.ball.biz.bet.order.bo.Handicap;
import com.ball.biz.exception.BizErrCode;
import lombok.extern.slf4j.Slf4j;

import java.math.BigDecimal;

/**
 * @author lhl
 * @date 2022/10/22 上午9:20
 */
@Slf4j
public class OverUnderAssist {

    /**
     * 目前大小球和让球盘口都可采用此分析模式
     * @param instantHandicap
     * @return
     */
    public static Handicap analyzeHandicap(String instantHandicap) {
        log.info("instantHandicap {}",instantHandicap);
        BizAssert.hasText(instantHandicap, BizErrCode.PARAM_ERROR_DESC,"instantHandicap");
        BigDecimal handicap = new BigDecimal(instantHandicap);
        return analyzeHandicap(handicap);

    }
    public static Handicap analyzeHandicap(BigDecimal handicap) {
        BizAssert.isTrue(handicap.compareTo(BigDecimal.ZERO) > 0, BizErrCode.ANALYZE_HANDICAP_ERROR, "instantHandicap");

        BigDecimal remainder = handicap.remainder(BigDecimal.ONE);
        BetType betType = null;
        if (remainder.compareTo(BigDecimal.ZERO) == 0 || remainder.compareTo(BigDecimal.valueOf(0.5)) == 0) {
            betType = BetType.ALL;
        } else if (remainder.compareTo(BigDecimal.valueOf(0.25)) == 0) {
            betType = BetType.AVG_ONE_HALF;
        } else if (remainder.compareTo(BigDecimal.valueOf(0.75)) == 0) {
            betType = BetType.AVG_HALF_ONE;
        }
        Double small = null;
        Double big;
        if (BetType.ALL == betType) {
            big = handicap.doubleValue();
        } else {
            small = handicap.doubleValue() - 0.25d;
            big = handicap.doubleValue() + 0.25d;
        }
        return Handicap.builder()
                .betType(betType)
                .big(big)
                .small(small)
                .build();
    }

    public static void main(String[] args) {
        System.out.println(JSON.toJSONString(analyzeHandicap("0.25")));
    }
}
