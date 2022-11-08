package com.ball.biz.bet.processor.assist;

import com.ball.biz.bet.enums.HandicapType;
import com.ball.biz.bet.enums.JobEnum;
import com.ball.biz.bet.enums.ScheduleStatus;
import com.ball.biz.exception.BizErrCode;
import com.ball.biz.exception.BizException;
import com.ball.biz.match.entity.JobMonitor;
import com.ball.biz.match.service.IJobMonitorService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * @author lhl
 * @date 2022/11/3 下午2:32
 */
@Slf4j
@Component
public class OddsAssist {
    @Autowired
    private IJobMonitorService jobMonitorService;

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

    /**
     * 赔率最新更新时间
     * fetch_odds_change_job  => ds_odds
     *
     * fetch_odds_score_half_prematch_job => ds_odds_score(dataType=2, status=1)
     * fetch_odds_score_half_inplay_job => ds_odds_score(dataType=2, status=2);
     * fetch_odds_score_prematch_job => ds_odds_score(dataType=1, status=1);
     * fetch_odds_score_inplay_job => ds_odds_score(dataType=1, status=2);
     * @return
     */
    public LocalDateTime getLastUpdateTime(HandicapType handicapType, Integer scoreStatus) {
        JobMonitor jobMonitor = jobMonitorService.queryOne(getJobEnum(handicapType, scoreStatus));
        // 如果没找到，返回一个过期时间
        return Optional.ofNullable(jobMonitor).map(JobMonitor::getLastResTime).orElse(LocalDateTime.now().plusYears(-1));
    }

    private JobEnum getJobEnum(HandicapType handicapType, Integer scoreStatus) {
        switch (handicapType) {
            case HANDICAP:
            case HANDICAP_HALF:
            case OVER_UNDER_HALF:
            case OVER_UNDER:
            case EUROPE_ODDS:
                return JobEnum.FETCH_ODDS_CHANGE_JOB;
            case CORRECT_SCORE_HALL:
                return scoreStatus == 1 ? JobEnum.FETCH_ODDS_SCORE_HALF_PREMATCH_JOB : JobEnum.FETCH_ODDS_SCORE_HALF_INPLAY_JOB;
            case CORRECT_SCORE:
                return scoreStatus == 1 ? JobEnum.FETCH_ODDS_SCORE_PREMATCH_JOB : JobEnum.FETCH_ODDS_SCORE_INPLAY_JOB;
                default:
                    throw new BizException(BizErrCode.NOT_FOUND_IMPL);
        }
    }
}
