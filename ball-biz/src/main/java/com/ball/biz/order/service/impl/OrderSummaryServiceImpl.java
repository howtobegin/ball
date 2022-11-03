package com.ball.biz.order.service.impl;

import com.ball.biz.order.entity.OrderInfo;
import com.ball.biz.order.entity.OrderSummary;
import com.ball.biz.order.mapper.OrderSummaryMapper;
import com.ball.biz.order.service.IOrderSummaryService;
import com.ball.biz.util.InsertHelper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

/**
 * <p>
 * 赛事结果概要 服务实现类
 * </p>
 *
 * @author lhl
 * @since 2022-11-01
 */
@Slf4j
@Service
public class OrderSummaryServiceImpl extends ServiceImpl<OrderSummaryMapper, OrderSummary> implements IOrderSummaryService {
    @Override
    public OrderSummary queryOne(LocalDate date, Integer sport) {
        return lambdaQuery().eq(OrderSummary::getSummaryDate, date)
                .eq(OrderSummary::getSport, sport)
                .one();
    }

    @Override
    public void newOrderCreate(OrderInfo order) {
        LocalDate betDate = order.getBetDate();
        Integer sport = order.getSport();
        log.info("orderId {} betDate {} sport {}", order.getId(), betDate, sport);
        OrderSummary exists = queryOne(betDate, sport);
        if (exists == null) {
            OrderSummary summary = new OrderSummary()
                    .setSummaryDate(betDate).setSport(sport)
                    .setUndoneCount(1L);
            boolean idempotentInsert = InsertHelper.idempotentInsert(this, summary);
            if (!idempotentInsert) {
                return;
            }
            exists = queryOne(betDate, sport);
        }
        boolean update = lambdaUpdate()
                .setSql("undone_count = undone_count + 1")

                .eq(OrderSummary::getId, exists.getId())
                .update();
        log.info("update {}",update);
    }

    @Override
    public void newOrderFinish(OrderInfo order) {
        LocalDate betDate = order.getBetDate();
        Integer sport = order.getSport();
        log.info("orderId {} betDate {} sport {}", order.getId(), betDate, sport);
        OrderSummary exists = queryOne(betDate, sport);
        if (exists == null) {
            if ((exists = idempotentInsert(order)) == null) {
                return;
            }
        }
        boolean update = lambdaUpdate()
                // 正常情况，一增一减，不可能出现负
                .setSql("undone_count = undone_count - 1")
                .setSql("complete_count = complete_count + 1")
                .last(" and undone_count > 0")

                .eq(OrderSummary::getId, exists.getId())
                .update();
        log.info("update {}",update);
    }

    private OrderSummary idempotentInsert(OrderInfo order) {
        LocalDate betDate = order.getBetDate();
        Integer sport = order.getSport();
        OrderSummary summary = new OrderSummary()
                .setSummaryDate(betDate).setSport(sport)
                .setUndoneCount(1L);
        boolean idempotentInsert = InsertHelper.idempotentInsert(this, summary);
        log.info("idempotentInsert {}", idempotentInsert);
        if (!idempotentInsert) {
            return null;
        }
        return queryOne(betDate, sport);
    }
}
