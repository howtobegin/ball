package com.ball.app;

import com.alibaba.fastjson.JSON;
import com.ball.biz.bet.enums.BetResult;
import com.ball.biz.bet.order.calculate.bo.CalcResult;
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
public class CalculatorTest {

    @Test
    public void testCalc() {
        BigDecimal betAmount = BigDecimal.valueOf(100);
        BigDecimal rate = BigDecimal.valueOf(1.2);
        for (BetResult betResult : BetResult.settled()) {
            CalcResult result = null;//CalculatorHolder.get(betResult).calc(betAmount, rate);
            log.info("result {}", JSON.toJSON(result));
        }
    }

}
