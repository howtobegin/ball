package com.ball.biz.bet.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 规则
 * @author lhl
 * @date 2022/10/17 上午10:14
 */
@Getter
@AllArgsConstructor
public enum RuleType {
    SINGLE("独赢"),
    SINGLE_HALF("独赢上半场"),
    HANDICAP("让球"),
    HANDICAP_HALF("让球上半场"),
    UNDER_OVER("大小球"),
    UNDER_OVER_HALF("大小球上半场"),
    SCORE("波胆"),
    SCORE_HALF("波胆上半场")
    ;

    private String desc;
}
