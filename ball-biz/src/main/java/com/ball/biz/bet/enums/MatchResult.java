package com.ball.biz.bet.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 比赛结果，用户投注都用这个
 * @author lhl
 * @date 2022/10/21 上午10:47
 */
@Getter
@AllArgsConstructor
public enum MatchResult {
    HOME,
    AWAY,
    // 基本上独赢才用到
    DRAW;
    public static MatchResult parse(Integer homeScore, Integer awayScore) {
        return homeScore > awayScore ? HOME : homeScore < awayScore ? AWAY : DRAW;
    }
}
