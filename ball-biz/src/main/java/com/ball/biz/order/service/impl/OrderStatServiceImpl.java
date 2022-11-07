package com.ball.biz.order.service.impl;

import com.alibaba.fastjson.JSON;
import com.ball.biz.order.bo.OrderStatUniqBo;
import com.ball.biz.order.entity.OrderInfo;
import com.ball.biz.order.entity.OrderStat;
import com.ball.biz.order.mapper.OrderStatMapper;
import com.ball.biz.order.service.IOrderStatService;
import com.ball.biz.util.InsertHelper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

/**
 * <p>
 * 订单代理商统计表 服务实现类
 * </p>
 *
 * @author lhl
 * @since 2022-11-01
 */
@Slf4j
@Service
public class OrderStatServiceImpl extends ServiceImpl<OrderStatMapper, OrderStat> implements IOrderStatService {
    @Override
    public OrderStat queryOne(OrderStatUniqBo uniqBo) {
        return lambdaQuery()
                .eq(OrderStat::getBetDate, uniqBo.getBetDate())
                .eq(uniqBo.getProxy1() != null, OrderStat::getProxy1, uniqBo.getProxy1())
                .eq(uniqBo.getProxy2() != null, OrderStat::getProxy2, uniqBo.getProxy2())
                .eq(uniqBo.getProxy3() != null, OrderStat::getProxy3, uniqBo.getProxy3())
                .eq(uniqBo.getBetCurrency() != null, OrderStat::getBetCurrency, uniqBo.getBetCurrency())
                .last("limit 1").one();
    }

    @Override
    public List<OrderStat> queryByDate(LocalDate start, LocalDate end, Long proxy1, Long proxy2, Long proxy3) {
        if (start == null && end == null) {
            return Lists.newArrayList();
        }
        return lambdaQuery()
                .ge(start != null, OrderStat::getBetDate, start)
                .ge(end != null, OrderStat::getBetDate, end)
                .eq(proxy1 != null, OrderStat::getProxy1, proxy1)
                .eq(proxy2 != null, OrderStat::getProxy2, proxy2)
                .eq(proxy3 != null, OrderStat::getProxy3, proxy3)
                .list();
    }

    @Override
    public void newOrderCreate(OrderInfo order) {
        OrderStatUniqBo uniqBo = buildUniqWhere(order);
        log.info("orderId {} uniqBo {}", order.getOrderId(), JSON.toJSONString(uniqBo));
        OrderStat exists = queryOne(uniqBo);
        if (exists == null) {
            OrderStat stat = buildOrderStat(order);
            boolean idempotentInsert = InsertHelper.idempotentInsert(this, stat);
            log.info("idempotentInsert {}", idempotentInsert);
            if (!idempotentInsert) {
                return;
            }
            exists = queryOne(uniqBo);
        }
        String betAmountStr = order.getBetAmount().stripTrailingZeros().toPlainString();

        boolean update = lambdaUpdate()
                .setSql("bet_amount = bet_amount + " + betAmountStr)
                .setSql("bet_count = bet_count + 1")

                .eq(OrderStat::getId, exists.getId())
                .update();
        log.info("update {}",update);
    }

    @Override
    public void newOrderFinish(OrderInfo order) {
        OrderStatUniqBo uniqBo = buildUniqWhere(order);
        log.info("orderId {} uniqBo {} ", order.getOrderId(), JSON.toJSON(uniqBo));
        OrderStat exists = queryOne(uniqBo);
        if (exists == null) {
            if ((exists = idempotentInsert(order)) ==null) {
                return;
            }
        }
        String resultAmountStr = order.getResultAmount().stripTrailingZeros().toPlainString();
        String validAmountStr = order.getValidAmount().stripTrailingZeros().toPlainString();
        String proxy1AmountStr = order.getProxy1Amount().stripTrailingZeros().toPlainString();
        String proxy2AmountStr = order.getProxy2Amount().stripTrailingZeros().toPlainString();
        String proxy3AmountStr = order.getProxy3Amount().stripTrailingZeros().toPlainString();
        String backwaterAmountStr = order.getBackwaterAmount().stripTrailingZeros().toPlainString();
        boolean update = lambdaUpdate()
                .setSql("result_amount = result_amount + " + resultAmountStr)
                .setSql("valid_amount = valid_amount + " + validAmountStr)
                .setSql("proxy1_amount = proxy1_amount + " + proxy1AmountStr)
                .setSql("proxy2_amount = proxy2_amount + " + proxy2AmountStr)
                .setSql("proxy3_amount = proxy3_amount + " + proxy3AmountStr)
                .setSql("backwater_amount = backwater_amount + " + backwaterAmountStr)

                .eq(OrderStat::getId, exists.getId())
                .update();
        log.info("update {}",update);
    }

    private OrderStat idempotentInsert(OrderInfo order) {LocalDate betDate = order.getBetDate();
        OrderStat stat = buildOrderStat(order);
        boolean idempotentInsert = InsertHelper.idempotentInsert(this, stat);
        log.info("idempotentInsert {}", idempotentInsert);
        if (!idempotentInsert) {
            return null;
        }
        return queryOne(buildUniqWhere(order));
    }

    private OrderStat buildOrderStat(OrderInfo order) {
        return new OrderStat()
                .setBetDate(order.getBetDate())
                .setProxy1(order.getProxy1())
                .setProxy2(order.getProxy2())
                .setProxy3(order.getProxy3())
                .setUserId(order.getUserId())
                .setBetCurrency(order.getBetCurrency())
                .setBetAmount(order.getBetAmount())
                .setResultAmount(order.getResultAmount())
                .setValidAmount(order.getValidAmount())
                .setProxy1Amount(order.getProxy1Amount())
                .setProxy2Amount(order.getProxy2Amount())
                .setProxy3Amount(order.getProxy3Amount())
                .setBackwaterAmount(order.getBackwaterAmount())
                .setBetCount(1L);
    }
    @Override
    public OrderStatUniqBo buildUniqWhere(OrderInfo order) {
        return OrderStatUniqBo.builder()
                .proxy1(order.getProxy1())
                .proxy2(order.getProxy2())
                .proxy3(order.getProxy3())
                .betDate(order.getBetDate())
                .userId(order.getUserId())
                .betCurrency(order.getBetCurrency())
                .build();
    }

    @Override
    public OrderStat sumByDateAndProxy(LocalDate start, LocalDate end, Long proxy1, Long proxy2, Long proxy3) {
        QueryWrapper<OrderStat> query = new QueryWrapper<>();

        query.select("sum(result_amount) result_amount, sum(valid_amount) valid_amount")
                .eq(proxy1 != null, "proxy1", proxy1)
                .eq(proxy2 != null, "proxy2", proxy2)
                .eq(proxy3 != null, "proxy3", proxy3)
                .ge("bet_date", start)
                .le("bet_date", end);
        return lambdaQuery().getBaseMapper().selectOne(query);
    }
}
