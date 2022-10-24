package com.ball.biz.bet.enums;

/**
 * @author lhl
 * @date 2022/10/21 下午7:04
 */
public enum BetOption {
    HOME,
    AWAY,
    DRAW,
    UNDER,
    OVER,
    SCORE,
    SCORE_OTHER;

    public boolean isMe(String name) {
        return name != null && name.equalsIgnoreCase(this.name());
    }
}
