package com.ball.biz.bet.enums;

import com.google.common.collect.Lists;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

/**
 * 投注结果
 * @author lhl
 * @date 2022/10/14 下午6:55
 */
@Getter
@AllArgsConstructor
public enum BetResult {
    // 未结算
    UNSETTLED("UNSETTLED", false),
    // 赢
    WIN("WIN", true),
    // 负
    LOSE("LOSE", true),
    // 半胜
    WIN_HALF("WIN_HALF", true),
    // 半负
    LOSE_HALF("LOSE_HALF", true),
    // 不赢不输
    DRAW("DRAW", false);

    private String code;
    /**
     * 是否退水
     */
    private boolean backwater;

    public static List<BetResult> settled() {
        return Lists.newArrayList(
                WIN,WIN_HALF,LOSE,LOSE_HALF,DRAW
        );
    }

    public static BetResult parse(String code) {
        for (BetResult e : values()) {
            if (e.code.equalsIgnoreCase(code)) {
                return e;
            }
        }
        return null;
    }
}
