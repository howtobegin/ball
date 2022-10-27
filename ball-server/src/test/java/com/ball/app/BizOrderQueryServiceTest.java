package com.ball.app;

import com.alibaba.fastjson.JSON;
import com.ball.app.controller.order.vo.OrderHistoryResp;
import com.ball.app.controller.order.vo.OrderResp;
import com.ball.app.service.BizOrderQueryService;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDate;
import java.util.List;

/**
 * @author lhl
 * @date 2022/10/27 下午3:50
 */
@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest(classes = BallServerApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class BizOrderQueryServiceTest {
    @Autowired
    private BizOrderQueryService bizOrderQueryService;

    @Test
    public void test() {
        LocalDate start = LocalDate.of(2022, 10, 26);
        LocalDate end = LocalDate.of(2022, 10, 26);
        List<OrderHistoryResp> history = bizOrderQueryService.history(9012448L, start, end);
        List<OrderResp> history2 = bizOrderQueryService.historyDate(9012448L, start);

        log.info("history {}", JSON.toJSONString(history));
        log.info("history2 {}", JSON.toJSONString(history2));
    }
}
