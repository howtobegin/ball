package com.ball.biz.bet.enums;

import com.ball.biz.exception.BizErrCode;
import com.ball.biz.exception.BizException;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author lhl
 * @date 2022/10/20 下午6:42
 */
@Getter
@AllArgsConstructor
public enum ScheduleStatus {
    // 0: Not started 1: First half 2: Half-time break 3: Second half 4: Extra time 5: Penalty -1: Finished -10: Cancelled -11: TBD -12: Terminated -13: Interrupted -14: Postponed
    // 未开始
    NOT_STARTED(0),
    // 上半场
    FIRST_HALF(1),
    // 半场休息
    HALF_TIME_BREAK(2),
    // 下半场
    SECOND_HALF(3),
    // 加时赛
    EXTRA_TIME(4),
    // 点球
    PENALTY(5),
    // 结束
    FINISHED(-1),
    // 取消
    CANCELLED(-10),
    // 待定
    TBD(-11),
    // 终止
    TERMINATED(-12),
    // 中断
    INTERRUPTED(-13),
    // 推迟
    POSTPONED(-14)
    ;
    private Integer code;

    public static ScheduleStatus parse(Integer code) {
        for (ScheduleStatus e : values()) {
            if (e.code.equals(code)) {
                return e;
            }
        }
        throw new BizException(BizErrCode.PARAM_ERROR_DESC);
    }

    public boolean isMe(Integer code) {
        return code != null && code.equals(this.code);
    }
}
