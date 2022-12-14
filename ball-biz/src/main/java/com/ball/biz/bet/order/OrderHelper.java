package com.ball.biz.bet.order;

import com.alibaba.fastjson.JSON;
import com.ball.base.exception.BizException;
import com.ball.biz.account.enums.PlayTypeEnum;
import com.ball.biz.bet.enums.BetType;
import com.ball.biz.bet.enums.HandicapType;
import com.ball.biz.bet.enums.OddsType;
import com.ball.biz.bet.order.bo.Handicap;
import com.ball.biz.bet.order.settle.assist.OverUnderAssist;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.util.Optional;

/**
 * @author lhl
 * @date 2022/10/21 下午5:11
 */
public class OrderHelper {
    public static <T> T parse(String oddsData, Class<T> clazz) {
        try {
            return !StringUtils.isEmpty(oddsData) ? JSON.parseObject(oddsData, clazz) : clazz.newInstance();
        } catch (Exception e) {
            throw new BizException("oddsData解析出错");
        }
    }

    public static String translate(String instantHandicap) {
        String handicapStr = null;
        if (!StringUtils.isEmpty(instantHandicap)) {
            Handicap handicap = OverUnderAssist.analyzeHandicap(new BigDecimal(instantHandicap).abs());
            String bigStr = doubleToString(handicap.getBig());
            String smallStr = doubleToString(handicap.getSmall());
            handicapStr = handicap.getBetType() == BetType.ALL ? bigStr : smallStr + "/" + bigStr;
            if (instantHandicap.startsWith("-")) {
                handicapStr = "-" + handicapStr;
            }
        }
        return handicapStr;
    }


    public static String doubleToString(Double value) {
        return Optional.ofNullable(value)
                .map(BigDecimal::valueOf)
                .map(BigDecimal::stripTrailingZeros)
                .map(BigDecimal::toPlainString)
                .orElse("");
    }

    public static PlayTypeEnum getPlayTypeEnum(HandicapType type, Integer oddsType) {
        switch (type) {
            case HANDICAP:
            case HANDICAP_HALF:
                return OddsType.IN_PLAY_ODDS.isMe(oddsType) ? PlayTypeEnum.HOE_INPAY : PlayTypeEnum.HOE;
            case OVER_UNDER:
            case OVER_UNDER_HALF:
                return PlayTypeEnum.HOE;
            case EUROPE_ODDS:
                return PlayTypeEnum.WINNER_AND_WINNER_INPAY;
            default:
                return OddsType.IN_PLAY_ODDS.isMe(oddsType) ? PlayTypeEnum.OTHER_INPAY : PlayTypeEnum.OTHER;
        }
    }
}
