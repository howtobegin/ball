package com.ball.biz.bet.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author lhl
 * @date 2022/10/21 下午6:29
 */
@Getter
@AllArgsConstructor
public enum OverUnder {
    OVER("OVER"),
    UNDER("UNDER");

    private String code;

    public static OverUnder parse(String code) {
        for (OverUnder e : values()) {
            if (e.code.equals(code)) {
                return e;
            }
        }
        return null;
    }
}
