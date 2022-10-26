package com.ball.biz.bet;

import com.ball.base.util.BizAssert;
import com.ball.biz.bet.enums.HandicapType;
import com.ball.biz.bet.enums.MatchTimeType;
import com.ball.biz.bet.enums.ScheduleStatus;
import com.ball.biz.bet.order.bo.BetBo;
import com.ball.biz.exception.BizErrCode;
import com.ball.biz.match.entity.Odds;
import com.ball.biz.match.entity.OddsScore;
import com.ball.biz.match.entity.Schedules;
import com.ball.biz.match.service.IOddsScoreService;
import com.ball.biz.match.service.IOddsService;
import com.ball.biz.match.service.ISchedulesService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.Optional;

/**
 * @author lhl
 * @date 2022/10/26 上午11:50
 */
@Slf4j
@Component
public class BetCheckAssist {
    @Autowired
    private ISchedulesService schedulesService;
    @Autowired
    private IOddsService oddsService;
    @Autowired
    private IOddsScoreService oddsScoreService;

    public void checkOddsAndSchedule(String bizNo, HandicapType handicapType) {
        // check odds
        Odds odds = oddsService.queryByBizNo(bizNo);
        BizAssert.notNull(odds, BizErrCode.DATA_NOT_EXISTS);
        log.info("odds type {} bo.type {}", odds.getType(), handicapType);
        BizAssert.isTrue(handicapType.isMe(odds.getType()), BizErrCode.PARAM_ERROR_DESC, "handicapType");
        // 未返回是否关闭，是否当未关闭处理？
        boolean isClose = odds.getIsClose() == null ? Boolean.FALSE : odds.getIsClose();
        log.info("matchId {} close {}",odds.getMatchId(), isClose);
        // 投注是否关闭
        BizAssert.isTrue(!isClose, BizErrCode.ODDS_CLOSE);
        // 是否维护
        boolean isMaintenance = odds.getMaintenance() == null ? Boolean.FALSE : odds.getMaintenance();
        BizAssert.isTrue(!isMaintenance, BizErrCode.ODDS_MAINTENANCE);

        // check schedules
        Schedules schedules = schedulesService.queryOne(odds.getMatchId());
        Integer status = schedules.getStatus();
        log.info("matchId {} status {}", schedules.getMatchId(), status);
        // 全场，校验状态
        if (handicapType.getMatchTimeType() == MatchTimeType.FULL) {
            BizAssert.isTrue(ScheduleStatus.canBetCodes().contains(status), BizErrCode.SCHEDULE_CANNT_BET);
        }
        // 半场，校验状态
        if (handicapType.getMatchTimeType() == MatchTimeType.HALF) {
            BizAssert.isTrue(ScheduleStatus.halfCanBetCodes().contains(status), BizErrCode.SCHEDULE_CANNT_BET);
        }
    }


    public void checkSchedule(BetBo bo) {
        checkSchedule(bo.getBizNo(), bo.getMatchId(), bo.getHandicapType());
    }
    public void checkSchedule(String bizNo, String matchId, HandicapType handicapType) {
        Schedules schedules = schedulesService.queryOne(getMatchId(bizNo, matchId));
        Integer status = schedules.getStatus();
        log.info("matchId {} status {}", schedules.getMatchId(), status);
        // 全场，校验状态
        if (handicapType.getMatchTimeType() == MatchTimeType.FULL) {
            BizAssert.isTrue(ScheduleStatus.canBetCodes().contains(status), BizErrCode.SCHEDULE_CANNT_BET);
        }
        // 半场，校验状态
        if (handicapType.getMatchTimeType() == MatchTimeType.HALF) {
            BizAssert.isTrue(ScheduleStatus.halfCanBetCodes().contains(status), BizErrCode.SCHEDULE_CANNT_BET);
        }
    }

    public String getMatchId(String bizNo, String matchId) {
        if (StringUtils.isEmpty(matchId)) {
            matchId = Optional.ofNullable(oddsService.queryByBizNo(bizNo)).map(Odds::getMatchId).orElse(null);
        }
        return matchId;
    }

    public void checkOdds(BetBo bo) {
        bo.setMatchId(checkOdds(bo.getBizNo(), bo.getHandicapType()));
    }

    public String checkOdds(String bizNo, HandicapType handicapType) {
        Odds odds = oddsService.queryByBizNo(bizNo);
        BizAssert.notNull(odds, BizErrCode.DATA_NOT_EXISTS);
        log.info("odds type {} bo.type {}", odds.getType(), handicapType);
        BizAssert.isTrue(handicapType.isMe(odds.getType()), BizErrCode.PARAM_ERROR_DESC, "handicapType");
        // 未返回是否关闭，是否当未关闭处理？
        boolean isClose = odds.getIsClose() == null ? Boolean.FALSE : odds.getIsClose();
        log.info("matchId {} close {}",odds.getMatchId(), isClose);
        // 投注是否关闭
        BizAssert.isTrue(!isClose, BizErrCode.ODDS_CLOSE);
        // 是否维护
        boolean isMaintenance = odds.getMaintenance() == null ? Boolean.FALSE : odds.getMaintenance();
        BizAssert.isTrue(!isMaintenance, BizErrCode.ODDS_MAINTENANCE);

        return odds.getMatchId();
    }

    public String checkOddsScore(String bizNo, Integer matchTimeType) {
        OddsScore oddsScore = oddsScoreService.queryByBizNo(bizNo);
        BizAssert.notNull(oddsScore, BizErrCode.DATA_NOT_EXISTS);
        BizAssert.isTrue(oddsScore.getType().equals(matchTimeType), BizErrCode.PARAM_ERROR_DESC, "bizNo或handicapType");
        boolean isClose = oddsScore.getIsClose();
        // 投注是否关闭
        BizAssert.isTrue(!isClose, BizErrCode.ODDS_CLOSE);
        return oddsScore.getMatchId();
    }
}
