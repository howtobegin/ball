package com.ball.biz.bet.processor;

import com.alibaba.fastjson.JSON;
import com.ball.base.util.BeanUtil;
import com.ball.base.util.BizAssert;
import com.ball.biz.bet.enums.BetOption;
import com.ball.biz.bet.enums.HandicapType;
import com.ball.biz.bet.enums.MatchTimeType;
import com.ball.biz.bet.order.bo.BetBo;
import com.ball.biz.bet.order.bo.OddsScoreData;
import com.ball.biz.bet.processor.bo.BetInfo;
import com.ball.biz.exception.BizErrCode;
import com.ball.biz.match.entity.OddsScore;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author lhl
 * @date 2022/10/22 上午11:30
 */
@Slf4j
@Component
public class CorrectScoreBetProcessor extends AbstractBetProcessor {
    @Override
    public HandicapType getHandicapType() {
        return HandicapType.CORRECT_SCORE;
    }

    @Override
    protected void checkOdds(BetBo bo) {
        OddsScore oddsScore = oddsScoreService.queryByBizNo(bo.getBizNo());
        BizAssert.notNull(oddsScore, BizErrCode.DATA_NOT_EXISTS);
        BizAssert.isTrue(oddsScore.getType().equals(getMatchTimeType()), BizErrCode.PARAM_ERROR_DESC, "bizNo或handicapType");
        boolean isClose = oddsScore.getIsClose();
        // 投注是否关闭
        BizAssert.isTrue(!isClose, BizErrCode.ODDS_CLOSE);
    }

    @Override
    protected BetInfo getBetInfo(BetBo bo) {
        OddsScore oddsScore = oddsScoreService.queryByBizNo(bo.getBizNo());
        String matchId = oddsScore.getMatchId();
        String companyId = oddsScore.getCompanyId();
        String betOddsStr = BetOption.SCORE == bo.getBetOption() ? oddsScore.getOdds() : oddsScore.getOtherOdds();
        log.info("type {} betOption {} betOdds {}", bo.getHandicapType(), bo.getBetOption(), betOddsStr);

        /**
         * 波胆投注需把其他投注选项保存下来
         */
        OddsScoreData oddsScoreData = BeanUtil.copy(oddsScore, OddsScoreData.class);
        List<OddsScore> oddsScores = oddsScoreService.queryByMatch(oddsScore.getMatchId(), getMatchTimeType());
        List<OddsScoreData.OddsScoreItem> items = oddsScores.stream().map(o -> BeanUtil.copy(o, OddsScoreData.OddsScoreItem.class)).collect(Collectors.toList());
        oddsScoreData.setOtherItems(items);
        String oddsData = JSON.toJSONString(oddsScoreData);
        return BetInfo.builder()
                .oddsData(oddsData)
                .matchId(matchId)
                .companyId(companyId)
                .betOddsStr(betOddsStr)
                .build();
    }

    protected Integer getMatchTimeType() {
        return MatchTimeType.FULL.getCode();
    }
}
