package com.ball.biz.bet.enums;

import com.ball.biz.bet.order.bo.OddsData;
import com.ball.biz.bet.order.bo.OddsScoreData;
import com.google.common.collect.Lists;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * 盘口类型
 * 增加盘口类型：
 * betOptionCheck
 * @author lhl
 * @date 2022/10/14 下午5:54
 */
@Getter
@AllArgsConstructor
public enum HandicapType {
    HANDICAP("HANDICAP","让球盘（亚盘）", OddsData.class, Lists.newArrayList(BetOption.HOME,BetOption.AWAY), MatchTimeType.FULL),
    HANDICAP_HALF("HANDICAP_HALF","半场让球盘（亚盘）", OddsData.class, Lists.newArrayList(BetOption.HOME,BetOption.AWAY), MatchTimeType.HALF),
    EUROPE_ODDS("EUROPE_ODDS","标准盘（欧赔）", OddsData.class, Lists.newArrayList(BetOption.HOME,BetOption.AWAY, BetOption.DRAW), MatchTimeType.FULL),
    OVER_UNDER("OVER_UNDER","大小球盘", OddsData.class, Lists.newArrayList(BetOption.OVER,BetOption.UNDER), MatchTimeType.FULL),
    OVER_UNDER_HALF("OVER_UNDER_HALF","半场大小球盘", OddsData.class, Lists.newArrayList(BetOption.OVER,BetOption.UNDER), MatchTimeType.HALF),
    CORRECT_SCORE("CORRECT_SCORE","波胆", OddsScoreData.class, Lists.newArrayList(BetOption.SCORE,BetOption.SCORE_OTHER), MatchTimeType.FULL),
    CORRECT_SCORE_HALL("CORRECT_SCORE_HALL","半场波胆", OddsScoreData.class, Lists.newArrayList(BetOption.SCORE,BetOption.SCORE_OTHER), MatchTimeType.HALF),
    ;

    private String code;
    private String desc;
    private Class oddsDataClass;
    // 投注选项
    private List<BetOption> betOptions;
    private MatchTimeType matchTimeType;

    public static HandicapType parse(String code) {
        for (HandicapType e : values()) {
            if (e.code.equalsIgnoreCase(code)) {
                return e;
            }
        }
        return null;
    }

    public boolean isMe(String code) {
        return code != null && code.equalsIgnoreCase(this.code);
    }

    /**
     * 全场/半场类型
     * @param matchTimeType
     * @return
     */
    public static List<String> fullTypes(MatchTimeType matchTimeType) {
        return Stream.of(values()).filter(e -> e.getMatchTimeType() == matchTimeType).map(HandicapType::getCode).collect(Collectors.toList());
    }
}
