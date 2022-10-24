package com.ball.biz.bet.enums;

import com.ball.biz.bet.order.bo.OddsData;
import com.ball.biz.bet.order.bo.OddsScoreData;
import com.google.common.collect.Lists;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

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
    HANDICAP("HANDICAP","让球盘（亚盘）", OddsData.class, Lists.newArrayList(BetOption.HOME,BetOption.AWAY)),
    HANDICAP_HALF("HANDICAP_HALF","半场让球盘（亚盘）", OddsData.class, Lists.newArrayList(BetOption.HOME,BetOption.AWAY)),
    EUROPE_ODDS("EUROPE_ODDS","标准盘（欧赔）", OddsData.class, Lists.newArrayList(BetOption.HOME,BetOption.AWAY, BetOption.DRAW)),
    OVER_UNDER("OVER_UNDER","大小球盘", OddsData.class, Lists.newArrayList(BetOption.OVER,BetOption.UNDER)),
    OVER_UNDER_HALF("OVER_UNDER_HALF","半场大小球盘", OddsData.class, Lists.newArrayList(BetOption.OVER,BetOption.UNDER)),
    CORRECT_SCORE("CORRECT_SCORE","波胆", OddsScoreData.class, Lists.newArrayList(BetOption.SCORE,BetOption.SCORE_OTHER)),
    CORRECT_SCORE_HALL("CORRECT_SCORE_HALL","半场波胆", OddsScoreData.class, Lists.newArrayList(BetOption.SCORE,BetOption.SCORE_OTHER)),
    ;

    private String code;
    private String desc;
    private Class oddsDataClass;
    // 投注选项
    private List<BetOption> betOptions;

    public static HandicapType parse(String code) {
        for (HandicapType e : values()) {
            if (e.code.equalsIgnoreCase(code)) {
                return e;
            }
        }
        return null;
    }

    public static List<BetResult> winOptions(HandicapType ruleType) {
        switch (ruleType) {
            case EUROPE_ODDS:
            case CORRECT_SCORE:
            case CORRECT_SCORE_HALL:
                return Lists.newArrayList(BetResult.WIN, BetResult.LOSE);
            case HANDICAP:
            case HANDICAP_HALF:
            case OVER_UNDER:
            case OVER_UNDER_HALF:
                // 全赢、全输、不赢不输、赢一半、输一半
                return Lists.newArrayList(BetResult.WIN, BetResult.LOSE, BetResult.DRAW, BetResult.WIN_HALF, BetResult.LOSE_HALF);
            default:
                return Lists.newArrayList();
        }
    }

    public boolean isMe(String code) {
        return code != null && code.equalsIgnoreCase(this.code);
    }
}
