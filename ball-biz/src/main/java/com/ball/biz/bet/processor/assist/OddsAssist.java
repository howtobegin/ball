package com.ball.biz.bet.processor.assist;

import com.ball.biz.bet.enums.HandicapType;
import com.ball.biz.bet.enums.OddsType;
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
        List<Integer> canBetCodes = ScheduleStatus.canBetCodes();
        switch (type) {
            case HANDICAP_HALF:
                // 半场让球的滚球玩法，可以在比赛进行中，投注；否则不让投
                if (OddsType.IN_PLAY_ODDS.getCode().equals(oddsType)) {
                    canBetCodes = ScheduleStatus.handicapHalfCanBetCodes();
                } else {
                    canBetCodes = ScheduleStatus.halfCanBetCodes();
                }
                break;
            case OVER_UNDER_HALF:
            case CORRECT_SCORE_HALL:
                canBetCodes = ScheduleStatus.halfCanBetCodes();
                break;
            case HANDICAP:
                // 全场让球的滚球玩法，可以在比赛进行中，投注；否则不让投
                if (OddsType.IN_PLAY_ODDS.getCode().equals(oddsType)) {
                    canBetCodes = ScheduleStatus.handicapCanBetCodes();
                } else {
                    canBetCodes = ScheduleStatus.canBetCodes();
                }
                break;
            case OVER_UNDER:
            case CORRECT_SCORE:
                canBetCodes = ScheduleStatus.canBetCodes();

        }
        return !canBetCodes.contains(scheduleStatus);
    }
}
