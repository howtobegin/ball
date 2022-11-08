package com.ball.app.runner;

import com.ball.biz.order.service.IOrderStatService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

/**
 * @author lhl
 * @date 2022/11/8 下午3:55
 */
@Slf4j
@Component
public class OrderStatRunner implements ApplicationRunner {
    @Autowired
    private IOrderStatService orderStatService;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        orderStatService.init();
    }
}
