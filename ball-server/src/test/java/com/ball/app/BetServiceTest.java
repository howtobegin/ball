package com.ball.app;

import com.ball.biz.bet.enums.BetOption;
import com.ball.biz.bet.enums.HandicapType;
import com.ball.biz.bet.order.bo.BetBo;
import com.ball.biz.bet.processor.BetProcessorHolder;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;

/**
 * @author lhl
 * @date 2022/10/17 上午11:36
 */
@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest(classes = BallServerApplication.class)
public class BetServiceTest {

    @Test
    public void testBet() {
        testHandicap();
        testOverUnder();
        testEuropeOdds();
    }

    @Test
    public void testHandicap() {
        testHandicap("6989167335500152832", HandicapType.HANDICAP);
        testHandicap("6989167335525318659", HandicapType.HANDICAP_HALF);

    }

    public void testHandicap(String bizNo, HandicapType type) {
        BetBo betBo = BetBo.builder()
                .userNo(9012433L)
                .handicapType(type)
                .bizNo(bizNo)
                .betOption(BetOption.HOME)
                .betAmount(BigDecimal.ONE)
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
                .userNo(9012433L)
                .handicapType(type)
                .bizNo(bizNo)
                .betOption(BetOption.UNDER)
                .betAmount(BigDecimal.ONE)
                .build();
        BetProcessorHolder.get(type).bet(betBo);
    }

    @Test
    public void testEuropeOdds() {
        testEuropeOdds(HandicapType.EUROPE_ODDS);
    }
    public void testEuropeOdds(HandicapType type) {
        BetBo betBo = BetBo.builder()
                .userNo(9012433L)
                .handicapType(type)
                .bizNo("6989167335516930048")
                .betOption(BetOption.DRAW)
                .betAmount(BigDecimal.TEN)
                .build();
        BetProcessorHolder.get(type).bet(betBo);
    }
    @Test
    public void testCorrectScore() {
        testCorrectScore(HandicapType.CORRECT_SCORE);
        testCorrectScore(HandicapType.CORRECT_SCORE_HALL);

    }
    public void testCorrectScore(HandicapType type) {
        BetBo betBo = BetBo.builder()
                .userNo(9012433L)
                .handicapType(type)
                .bizNo("6989167335516930048")
                .betOption(BetOption.DRAW)
                .betAmount(BigDecimal.TEN)
                .build();
        BetProcessorHolder.get(type).bet(betBo);
    }
}
