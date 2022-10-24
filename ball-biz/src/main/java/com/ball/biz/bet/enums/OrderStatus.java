package com.ball.biz.bet.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author lhl
 * @date 2022/10/19 下午6:54
 */
@Getter
@AllArgsConstructor
public enum OrderStatus {
    // 初始化，待确认
    INIT("INIT", false),
    // 已确认，等待比赛结果结算
    CONFIRM("CONFIRM", false),
    // 已结算（仅仅比赛结果结算），等待派奖
    SETTLED("SETTLED", false),
    // 完成
    FINISH("FINISH", true),
    // 取消
    CANCEL("CANCEL", true),
    // 因比赛中断取消
    MATCH_CANCEL("MATCH_CANCEL", true)
    ;

    private String code;
    private boolean finish;
}
