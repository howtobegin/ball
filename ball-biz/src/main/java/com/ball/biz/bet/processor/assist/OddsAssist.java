package com.ball.biz.bet.processor.assist;

import com.ball.biz.bet.enums.HandicapType;
import com.ball.biz.bet.enums.ScheduleStatus;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

/**
 * @author lhl
 * @date 2022/11/3 下午2:32
 */
@Slf4j
public class OddsAssist {

    /**
     * 重新判定未关闭的赔率是否关闭
     * 1、如果半场，只有让球的滚球玩法可在进行中投注，其他都只能在开始前投
     * @param close
     * @param scheduleStatus
     * @param type
     * @param oddsType          注意区分主要玩法和波胆
     * @return
     */
    public static boolean rejudgmentClose(Boolean close, Integer scheduleStatus, HandicapType type, Integer oddsType) {
        if (close != null && close) {
            return close;
        }
        List<Integer> canBetCodes = type.full() ? ScheduleStatus.canBetCodes() : ScheduleStatus.halfCanBetCodes();
        return !canBetCodes.contains(scheduleStatus);
    }
}
