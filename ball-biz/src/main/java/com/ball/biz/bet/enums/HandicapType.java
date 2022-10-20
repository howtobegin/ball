package com.ball.biz.bet.enums;

import com.google.common.collect.Lists;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

/**
 * 盘口类型
 * @author lhl
 * @date 2022/10/14 下午5:54
 */
@Getter
@AllArgsConstructor
public enum HandicapType {
    HANDICAP("HANDICAP","让球盘（亚盘）"),
    HANDICAP_HALF("HANDICAP_HALF","半场让球盘（亚盘）"),
    EUROPE_ODDS("EUROPE_ODDS","标准盘（欧赔）"),
    OVER_UNDER("OVER_UNDER","大小球盘"),
    OVER_UNDER_HALF("OVER_UNDER_HALF","半场大小球盘"),
    CORRECT_SCORE("CORRECT_SCORE","波胆"),
    CORRECT_SCORE_HALL("CORRECT_SCORE_HALL","半场波胆"),
    ;

    private String code;
    private String desc;

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
}
