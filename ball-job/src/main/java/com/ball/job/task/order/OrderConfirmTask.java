package com.ball.job.task.order;

import com.ball.biz.bet.order.job.OrderConfirmService;
import com.ball.job.base.BaseBizTask;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * @author lhl
 * @date 2022/10/20 下午6:23
 */
@Slf4j
@Component
public class OrderConfirmTask extends BaseBizTask {
    @Autowired
    private OrderConfirmService orderConfirmService;

    @Value("${job.order.confirm.enabled:true}")
    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    @Value("${job.order.confirm.expression:*/2 * * * * ?}")
    public void setExpression(String expression) {
        super.setExpression(expression);
    }

    @Override
    public void execute() {
        orderConfirmService.execute();
    }
}
