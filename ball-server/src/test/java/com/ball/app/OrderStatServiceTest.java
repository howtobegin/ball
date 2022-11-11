package com.ball.app;

import com.ball.biz.order.service.IOrderStatService;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @author lhl
 * @date 2022/10/17 上午11:36
 */
@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest(classes = BallServerApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class OrderStatServiceTest {
    @Autowired
    private IOrderStatService orderStatService;

    @Test
    public void testInit() {
        orderStatService.init();
    }
}
