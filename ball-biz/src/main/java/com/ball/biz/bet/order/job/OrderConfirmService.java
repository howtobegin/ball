package com.ball.biz.bet.order.job;

import com.ball.biz.bet.enums.OrderStatus;
import com.ball.biz.match.service.IOddsScoreService;
import com.ball.biz.match.service.IOddsService;
import com.ball.biz.order.entity.OrderInfo;
import com.ball.biz.order.service.IOrderInfoService;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author lhl
 * @date 2022/10/20 下午6:27
 */
@Slf4j
@Getter
@Component
public class OrderConfirmService extends BaseJobService<OrderInfo> {
    @Autowired
    private IOrderInfoService orderInfoService;
    @Autowired
    private IOddsService oddsService;
    @Autowired
    private IOddsScoreService oddsScoreService;

    @Value("${order.confirm.page.size:100}")
    private int pageSize;
    /**
     * 确认时间
     */
    @Value("${order.confirm.seconds:5}")
    private int confirmSeconds;

    @Override
    public boolean executeOne(OrderInfo data) {
        // TODO 校验投注时，比分或赔率，决定是否取消订单



        OrderStatus next = OrderStatus.CONFIRM;
        orderInfoService.updateStatus(data.getOrderId(), OrderStatus.INIT, next);
        return true;
    }

    @Override
    public List<OrderInfo> fetchData() {
        log.info("maxCallbackId {} pageSize {}", maxCallbackId, pageSize);
        return orderInfoService.lambdaQuery()
                .gt(OrderInfo::getId, maxCallbackId)
                .lt(OrderInfo::getCreateTime, LocalDateTime.now().minusSeconds(confirmSeconds))
                .eq(OrderInfo::getStatus, OrderStatus.INIT.getCode())
                .orderByAsc(OrderInfo::getId)
                .page(new Page<>(1, pageSize))
                .getRecords();
    }

    @Override
    public Long getId(OrderInfo data) {
        return data.getId();
    }

    @Override
    public String getBizNo(OrderInfo data) {
        return data.getOrderId();
    }
}
