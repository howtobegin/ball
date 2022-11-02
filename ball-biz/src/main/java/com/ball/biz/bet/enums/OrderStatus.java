package com.ball.biz.bet.enums;

import com.google.common.collect.Lists;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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
    public static OrderStatus parse(String code) {
        for (OrderStatus e : values()) {
            if (e.getCode().equalsIgnoreCase(code)) {
                return e;
            }
        }
        return null;
    }

    public boolean isMe(String code) {
        return code != null && this.code.equalsIgnoreCase(code);
    }

    public static List<String> finishCodes(boolean finish) {
        return Stream.of(values()).filter(e -> e.finish == finish).map(OrderStatus::getCode).collect(Collectors.toList());
    }

    public static List<String> cancelCodes() {
        return Lists.newArrayList(CANCEL.code, MATCH_CANCEL.code);
    }

    public static boolean isCancel(OrderStatus status) {
        return cancelCodes().contains(status.getCode());
    }
}
