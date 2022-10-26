package com.ball.app;

import com.ball.biz.bet.enums.BetOption;
import com.ball.biz.bet.enums.HandicapType;
import com.ball.biz.bet.order.bo.BetBo;
import com.ball.biz.bet.processor.BetProcessorHolder;
import com.ball.biz.match.entity.Odds;
import com.ball.biz.match.entity.OddsScore;
import com.ball.biz.match.entity.Schedules;
import com.ball.biz.match.service.IOddsScoreService;
import com.ball.biz.match.service.IOddsService;
import com.ball.biz.match.service.ISchedulesService;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * @author lhl
 * @date 2022/10/17 上午11:36
 */
@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest(classes = BallServerApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class BetServiceTest {
    @Autowired
    private ISchedulesService schedulesService;
    @Autowired
    private IOddsService oddsService;
    @Autowired
    private IOddsScoreService oddsScoreService;

    private static final Long USER_NO2= 9012457L;

    private static final Long USER_NO= 9012448L;

    @Test
    public void testRandomBet() {
        LocalDateTime start = LocalDateTime.now();
        LocalDateTime end = start.plusDays(4);
        List<Schedules> schedules = schedulesService.queryByDate(start, end);
        for (Schedules schedule : schedules) {
            testBetAll(schedule.getMatchId());
        }
    }

    @Test
    public void testBetMatch() {
        String matchId = "257631226";
        testBetAll(matchId);
    }

    /**
     * 254940327
     * @param matchId
     */
    public void testBetAll(String matchId) {
        List<Odds> odds = oddsService.queryByMatchId(matchId);
//        for (Odds odd : odds) {
//            HandicapType type = HandicapType.parse(odd.getType());
//            switch (type) {
//                case HANDICAP:
//                case HANDICAP_HALF:
//                    testHandicap(odd.getBizNo(), type);
//                    break;
//                case OVER_UNDER_HALF:
//                case OVER_UNDER:
//                    testOverUnder(odd.getBizNo(), type);
//                    break;
//                case EUROPE_ODDS:
//                    testEuropeOdds(odd.getBizNo(), type);
//            }
//        }

        List<OddsScore> oddsScores = oddsScoreService.queryByMatch(matchId, null);
        for (OddsScore oddsScore : oddsScores) {
            HandicapType type = oddsScore.getType() == 1 ? HandicapType.CORRECT_SCORE : HandicapType.CORRECT_SCORE_HALL;
            testCorrectScore(oddsScore.getBizNo().toString(), type);
        }
    }

    @Test
    public void testScoreRandomBet() {

    }

    private BetBo buildBetBo(String bizNo, HandicapType type) {
        return BetBo.builder()
                .userNo(USER_NO)
                .handicapType(type)
                .bizNo(bizNo)
                .betOption(BetOption.HOME)
                .betAmount(BigDecimal.ONE)
                .build();
    }

    @Test
    public void testBet() {
        testHandicap();
        testOverUnder();
        testEuropeOdds();
    }

    @Test
    public void testHandicap() {
        testHandicap("6989167539829866504", HandicapType.HANDICAP);
        //testHandicap("6990696980376715310", HandicapType.HANDICAP_HALF);

    }

    public void testHandicap(String bizNo, HandicapType type) {
        BetBo betBo = BetBo.builder()
                .userNo(USER_NO)
                .handicapType(type)
                .bizNo(bizNo)
                .betOption(BetOption.HOME)
                .betAmount(BigDecimal.TEN.multiply(BigDecimal.TEN))
                .build();
        BetProcessorHolder.get(type).bet(betBo);
        betBo = BetBo.builder()
                .userNo(USER_NO)
                .handicapType(type)
                .bizNo(bizNo)
                .betOption(BetOption.AWAY)
                .betAmount(BigDecimal.TEN.multiply(BigDecimal.TEN))
                .build();
        BetProcessorHolder.get(type).bet(betBo);
    }

    @Test
    public void testOverUnder() {
        testOverUnder("6989167335516930049", HandicapType.OVER_UNDER);
        testOverUnder("6989167355964162050", HandicapType.OVER_UNDER_HALF);

    }

    public void testOverUnder(String bizNo, HandicapType type) {
        BetBo betBo = BetBo.builder()
                .userNo(USER_NO)
                .handicapType(type)
                .bizNo(bizNo)
                .betOption(BetOption.UNDER)
                .betAmount(BigDecimal.TEN.multiply(BigDecimal.TEN))
                .build();
        BetProcessorHolder.get(type).bet(betBo);
        betBo = BetBo.builder()
                .userNo(USER_NO)
                .handicapType(type)
                .bizNo(bizNo)
                .betOption(BetOption.OVER)
                .betAmount(BigDecimal.TEN.multiply(BigDecimal.TEN))
                .build();
        BetProcessorHolder.get(type).bet(betBo);
    }

    @Test
    public void testEuropeOdds() {
        testEuropeOdds("6989167335516930048",HandicapType.EUROPE_ODDS);
    }
    public void testEuropeOdds(String bizNo, HandicapType type) {
        BetBo betBo = BetBo.builder()
                .userNo(USER_NO)
                .handicapType(type)
                .bizNo(bizNo)
                .betOption(BetOption.DRAW)
                .betAmount(BigDecimal.TEN.multiply(BigDecimal.TEN))
                .build();
        BetProcessorHolder.get(type).bet(betBo);
        betBo = BetBo.builder()
                .userNo(USER_NO)
                .handicapType(type)
                .bizNo(bizNo)
                .betOption(BetOption.HOME)
                .betAmount(BigDecimal.TEN.multiply(BigDecimal.TEN))
                .build();
        BetProcessorHolder.get(type).bet(betBo);
        betBo = BetBo.builder()
                .userNo(USER_NO)
                .handicapType(type)
                .bizNo(bizNo)
                .betOption(BetOption.AWAY)
                .betAmount(BigDecimal.TEN.multiply(BigDecimal.TEN))
                .build();
        BetProcessorHolder.get(type).bet(betBo);
    }
    @Test
    public void testCorrectScore() {
        testCorrectScore("6989167335516930048",HandicapType.CORRECT_SCORE);
        testCorrectScore("6989167335516930048",HandicapType.CORRECT_SCORE_HALL);

    }
    public void testCorrectScore(String bizNo, HandicapType type) {
        BetBo betBo = BetBo.builder()
                .userNo(USER_NO)
                .handicapType(type)
                .bizNo(bizNo)
                .betOption(BetOption.SCORE)
                .betAmount(BigDecimal.TEN.multiply(BigDecimal.TEN))
                .build();
        BetProcessorHolder.get(type).bet(betBo);
    }
}
