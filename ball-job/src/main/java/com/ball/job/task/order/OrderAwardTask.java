package com.ball.job.task.order;

import com.ball.biz.bet.order.job.OrderAwardService;
import com.ball.job.base.BaseBizTask;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * 派奖任务
 * @author lhl
 * @date 2022/10/20 下午6:23
 */
@Slf4j
@Component
public class OrderAwardTask extends BaseBizTask {
    @Autowired
    private OrderAwardService orderAwardService;

    @Value("${job.order.award.enabled:true}")
    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    @Value("${job.order.award.expression:*/2 * * * * ?}")
    public void setExpression(String expression) {
        super.setExpression(expression);
    }

    @Override
    public void execute() {
        orderAwardService.execute();
    }
}
