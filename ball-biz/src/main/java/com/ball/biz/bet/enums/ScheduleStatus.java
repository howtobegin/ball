package com.ball.biz.bet.enums;

import com.google.common.collect.Lists;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

/**
 * @author lhl
 * @date 2022/10/20 下午6:42
 */
@Getter
@AllArgsConstructor
public enum ScheduleStatus {
    // 0: Not started 1: First half 2: Half-time break 3: Second half 4: Extra time 5: Penalty -1: Finished -10: Cancelled -11: TBD -12: Terminated -13: Interrupted -14: Postponed
    // 未开始
    NOT_STARTED(0, "未开始"),
    // 上半场
    FIRST_HALF(1, "上半场"),
    // 半场休息
    HALF_TIME_BREAK(2, "半场休息"),
    // 下半场
    SECOND_HALF(3, "下半场"),
    // 加时赛
    EXTRA_TIME(4, "加时赛"),
    // 点球
    PENALTY(5, "点球"),
    // 结束
    FINISHED(-1, "结束"),
    // 取消
    CANCELLED(-10, "取消"),
    // 待定
    TBD(-11, "待定"),
    // 终止
    TERMINATED(-12, "终止"),
    // 中断
    INTERRUPTED(-13, "中断"),
    // 推迟
    POSTPONED(-14, "推迟")
    ;
    private Integer code;
    private String desc;

    public static ScheduleStatus parse(Integer code) {
        for (ScheduleStatus e : values()) {
            if (e.code.equals(code)) {
                return e;
            }
        }
        return null;
    }

    public boolean isMe(Integer code) {
        return code != null && code.equals(this.code);
    }

    /**
     * 半场结算状态
     * @return
     */
    public static List<Integer> halfSettleCodes() {
        return Lists.newArrayList(HALF_TIME_BREAK.code, SECOND_HALF.code, EXTRA_TIME.code, PENALTY.code, FINISHED.code);
    }

    /**
     * 全场，进行中的状态
     * @return
     */
    public static List<Integer> inplayCodes() {
        return Lists.newArrayList(FIRST_HALF.code, HALF_TIME_BREAK.code, SECOND_HALF.code);
    }

    /**
     * 普通半场可投注状态
     * @return
     */
    public static List<Integer> halfCanBetCodes() {
        return Lists.newArrayList(NOT_STARTED.code);
    }

    /**
     * 滚球（目前只有让球盘）半场可投注状态
     * @return
     */
    public static List<Integer> handicapHalfCanBetCodes() {
        return Lists.newArrayList(NOT_STARTED.code, FIRST_HALF.code);
    }

    /**
     * 普通全场可投注状态
     * @return
     */
    public static List<Integer> canBetCodes() {
        return Lists.newArrayList(NOT_STARTED.code);
    }

    /**
     * 让球的滚球玩法，全场可投注状态
     * @return
     */
    public static List<Integer> handicapCanBetCodes() {
        return Lists.newArrayList(NOT_STARTED.code, FIRST_HALF.code, HALF_TIME_BREAK.code,SECOND_HALF.code);
    }
}
