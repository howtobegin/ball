package com.ball.job.task.order;

import com.ball.biz.bet.order.job.OrderSettleService;
import com.ball.job.base.BaseBizTask;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * 订单结算
 * @author lhl
 * @date 2022/10/20 下午6:23
 */
@Slf4j
@Component
public class OrderSettleTask extends BaseBizTask {
    @Autowired
    private OrderSettleService orderSettleService;

    @Value("${job.order.settle.enabled:true}")
    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    @Value("${job.order.settle.expression:*/2 * * * * ?}")
    public void setExpression(String expression) {
        super.setExpression(expression);
    }

    @Override
    public void execute() {
        orderSettleService.execute();
    }
}
