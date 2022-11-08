package com.ball.app;

import com.ball.biz.bet.enums.BetType;
import com.ball.biz.bet.enums.HandicapType;
import com.ball.biz.bet.order.settle.analyze.Analyzer;
import com.ball.biz.bet.order.settle.analyze.AnalyzerHolder;
import com.ball.biz.bet.order.settle.analyze.bo.AnalyzeResult;
import com.ball.biz.bet.order.settle.parse.Parser;
import com.ball.biz.bet.order.settle.parse.ParserHolder;
import com.ball.biz.order.entity.OrderInfo;
import com.ball.biz.order.service.IOrderInfoService;
import lombok.extern.slf4j.Slf4j;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * @author lhl
 * @date 2022/10/24 下午3:48
 */
@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest(classes = BallServerApplication.class)
public class AnalyzerTest {
    @Autowired
    private IOrderInfoService orderInfoService;

    @Test
    public void testHandicap() {
        // 让球
        String orderId = "1583449776718913538";
        OrderInfo order = orderInfoService.queryByOrderId(orderId);
        HandicapType type = HandicapType.parse(order.getHandicapType());
        AnalyzeResult betResult = AnalyzerHolder.get(type).analyze(order);
        log.info("orderId {} dataType {} betResult {}", orderId, type, betResult);
    }

    @Test
    public void testAll() {
        List<OrderInfo> orders = orderInfoService.lambdaQuery().list();
        for (OrderInfo order : orders) {
            HandicapType type = HandicapType.parse(order.getHandicapType());
            AnalyzeResult betResult = AnalyzerHolder.get(type).analyze(order);
        }
    }

    @Test
    public void testHowMany() {
        Map<HandicapType, Analyzer> map = AnalyzerHolder.all();
        map.forEach((k,v) -> {
            log.info("Analyzer {} {}",k,v.getHandicapType());
            Assert.assertSame(k, v.getHandicapType());
        });
        Map<HandicapType, Map<BetType, Parser>> map2 = ParserHolder.all();
        map2.forEach((k, v) -> {
            Iterator<BetType> it = v.keySet().iterator();
            while (it.hasNext()) {
                BetType betType = it.next();
                Parser parser = v.get(betType);
                log.info("Parser {} {} {} {}",k,betType, parser.getHandicapType(), parser.getBetType());
                Assert.assertSame(k, parser.getHandicapType());
                Assert.assertSame(betType, parser.getBetType());
            }
        });
    }
}
