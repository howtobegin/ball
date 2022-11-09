package com.ball.biz.order.service.impl;

import com.alibaba.fastjson.JSON;
import com.ball.base.util.BizAssert;
import com.ball.biz.account.service.ICurrencyService;
import com.ball.biz.bet.enums.OrderStatus;
import com.ball.biz.enums.CurrencyEnum;
import com.ball.biz.exception.BizErrCode;
import com.ball.biz.order.bo.OrderStatUniqBo;
import com.ball.biz.order.entity.OrderInfo;
import com.ball.biz.order.entity.OrderStat;
import com.ball.biz.order.mapper.OrderStatMapper;
import com.ball.biz.order.service.IOrderInfoService;
import com.ball.biz.order.service.IOrderStatService;
import com.ball.biz.util.InsertHelper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

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
    @Autowired
    private ICurrencyService currencyService;
    @Autowired
    private IOrderInfoService orderInfoService;

    @Override
    public void init() {
        try {
            // 如果没数据，重新统计写入
            OrderStat one = lambdaQuery().last("limit 1").one();
            log.info("exists {}", one != null);
            if (one != null) {
                return;
            }
            // 可能很慢，只需执行一次
            QueryWrapper<OrderInfo> query = new QueryWrapper<>();
            query.select("proxy1,proxy2,proxy3,bet_date,user_id,bet_currency," +
                    "sum(bet_amount) bet_amount," +
                    "sum(result_amount) result_amount," +
                    "sum(valid_amount) valid_amount," +
                    "sum(proxy1_amount) proxy1_amount," +
                    "sum(proxy2_amount) proxy2_amount," +
                    "sum(proxy3_amount) proxy3_amount," +
                    "sum(backwater_amount) backwater_amount," +
                    // id接收投注数量
                    "sum(1) id")
                    .eq("status", OrderStatus.FINISH.getCode())
                    .groupBy("proxy1,proxy2,proxy3,bet_date,user_id,bet_currency").orderByAsc("bet_date");
            List<OrderInfo> orders = orderInfoService.getBaseMapper().selectList(query);
            log.info("orders's size {}", orders.size());
            // 初始化
            List<OrderStat> orderStats = orders.stream().map(o -> buildOrderStat(o, o.getId())).collect(Collectors.toList());
            log.info("orderStats's size {}", orders.size());
            saveBatch(orderStats);
            log.info("init end");
        } catch (Exception e) {
            log.info("init error {}", e.getMessage(), e);
        }
    }

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
        String betAmountStr = toPlainString(order.getBetAmount());
        BigDecimal rmbRate = currencyService.getRmbRate(order.getBetCurrency());
        String betRmbAmountStr = toPlainString(order.getBetAmount().multiply(rmbRate));

        boolean update = lambdaUpdate()
                .setSql("bet_amount = bet_amount + " + betAmountStr)
                .setSql("bet_rmb_amount = bet_rmb_amount + " + betRmbAmountStr)
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
        String betCurrency = order.getBetCurrency();

        String resultAmountStr = toPlainString(order.getResultAmount());
        String resultRmbAmountStr = toPlainString(calcRmb(order.getResultAmount(), betCurrency));
        String validAmountStr = toPlainString(order.getValidAmount());
        String validRmbAmountStr = toPlainString(calcRmb(order.getValidAmount(), betCurrency));
        String proxy1AmountStr = toPlainString(order.getProxy1Amount());
        String proxy1RmbAmountStr = toPlainString(calcRmb(order.getProxy1Amount(), betCurrency));
        String proxy2AmountStr = toPlainString(order.getProxy2Amount());
        String proxy2RmbAmountStr = toPlainString(calcRmb(order.getProxy2Amount(), betCurrency));
        String proxy3AmountStr = toPlainString(order.getProxy3Amount());
        String proxy3RmbAmountStr = toPlainString(calcRmb(order.getProxy3Amount(), betCurrency));
        String backwaterAmountStr = toPlainString(order.getBackwaterAmount());
        String backwaterRmbAmountStr = toPlainString(calcRmb(order.getBackwaterAmount(), betCurrency));
        boolean update = lambdaUpdate()
                .setSql("result_amount = result_amount + " + resultAmountStr)
                .setSql("result_rmb_amount = result_rmb_amount + " + resultRmbAmountStr)
                .setSql("valid_amount = valid_amount + " + validAmountStr)
                .setSql("valid_rmb_amount = valid_rmb_amount + " + validRmbAmountStr)
                .setSql("proxy1_amount = proxy1_amount + " + proxy1AmountStr)
                .setSql("proxy1_rmb_amount = proxy1_rmb_amount + " + proxy1RmbAmountStr)
                .setSql("proxy2_amount = proxy2_amount + " + proxy2AmountStr)
                .setSql("proxy2_rmb_amount = proxy2_rmb_amount + " + proxy2RmbAmountStr)
                .setSql("proxy3_amount = proxy3_amount + " + proxy3AmountStr)
                .setSql("proxy3_rmb_amount = proxy3_rmb_amount + " + proxy3RmbAmountStr)
                .setSql("backwater_amount = backwater_amount + " + backwaterAmountStr)
                .setSql("backwater_rmb_amount = backwater_rmb_amount + " + backwaterRmbAmountStr)

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

    @Override
    public OrderStat buildOrderStat(OrderInfo order) {
        return buildOrderStat(order, 1L);
    }

    @Override
    public OrderStat buildOrderStat(OrderInfo order, Long betCount) {
        String betCurrency = order.getBetCurrency();
        return new OrderStat()
                .setBetDate(order.getBetDate())
                .setProxy1(order.getProxy1())
                .setProxy2(order.getProxy2())
                .setProxy3(order.getProxy3())
                .setUserId(order.getUserId())
                .setBetCurrency(betCurrency)

                .setBetAmount(order.getBetAmount())
                .setBetRmbAmount(calcRmb(order.getBetAmount(), betCurrency))
                .setResultAmount(order.getResultAmount())
                .setResultRmbAmount(calcRmb(order.getResultAmount(), betCurrency))
                .setValidAmount(order.getValidAmount())
                .setValidRmbAmount(calcRmb(order.getValidAmount(), betCurrency))
                .setProxy1Amount(order.getProxy1Amount())
                .setProxy1RmbAmount(calcRmb(order.getProxy1Amount(), betCurrency))
                .setProxy2Amount(order.getProxy2Amount())
                .setProxy2RmbAmount(calcRmb(order.getProxy2Amount(), betCurrency))
                .setProxy3Amount(order.getProxy3Amount())
                .setProxy3RmbAmount(calcRmb(order.getProxy3Amount(), betCurrency))
                .setBackwaterAmount(order.getBackwaterAmount())
                .setBackwaterRmbAmount(calcRmb(order.getBackwaterAmount(), betCurrency))
                .setBetCount(betCount);
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
    public OrderStat sumRmbByDateAndProxy(LocalDate start, LocalDate end, Long proxy1, Long proxy2, Long proxy3) {
        QueryWrapper<OrderStat> query = new QueryWrapper<>();
        query.select(sumRmbSelect())
                .eq(proxy1 != null, "proxy1", proxy1)
                .eq(proxy2 != null, "proxy2", proxy2)
                .eq(proxy3 != null, "proxy3", proxy3)
                .ge("bet_date", start)
                .le("bet_date", end);
        return lambdaQuery().getBaseMapper().selectOne(query);
    }

    @Override
    public List<OrderStat> sumRmbGroupByDate(LocalDate start, LocalDate end, Long proxy1, Long proxy2, Long proxy3) {
        QueryWrapper<OrderStat> query = new QueryWrapper<>();
        query.select("bet_date," + sumRmbSelect())
                .eq(proxy1 != null, "proxy1", proxy1)
                .eq(proxy2 != null, "proxy2", proxy2)
                .eq(proxy3 != null, "proxy3", proxy3)
                .ge("bet_date", start)
                .le("bet_date", end)
                .groupBy("bet_date");
        return lambdaQuery().getBaseMapper().selectList(query);
    }

    @Override
    public List<OrderStat> sumRmbGroupByProxy(LocalDate start, LocalDate end, Long proxy1, Long proxy2, Long proxy3, int level) {
        String groupBy = "";
        if (level == 1) {
            groupBy = "proxy1";
        } else if (level == 2) {
            groupBy = "proxy1, proxy2";
        } else if (level == 3) {
            groupBy = "proxy1, proxy2, proxy3";
        }
        return sumRmbGroupBy(start, end, proxy1, proxy2, proxy3, groupBy);
    }

    @Override
    public List<OrderStat> sumRmbGroupByUser(LocalDate start, LocalDate end, Long proxy1, Long proxy2, Long proxy3) {
        BizAssert.notNull(start, BizErrCode.PARAM_ERROR_DESC,"start");
        BizAssert.notNull(end, BizErrCode.PARAM_ERROR_DESC,"end");
        BizAssert.notNull(proxy1, BizErrCode.PARAM_ERROR_DESC,"proxy1");
        BizAssert.notNull(proxy2, BizErrCode.PARAM_ERROR_DESC,"proxy2");
        BizAssert.notNull(proxy3, BizErrCode.PARAM_ERROR_DESC,"proxy3");

        return sumRmbGroupBy(start, end, proxy1, proxy2, proxy3, "user_id");
    }

    private List<OrderStat> sumRmbGroupBy(LocalDate start, LocalDate end, Long proxy1, Long proxy2, Long proxy3, String groupBy) {
        QueryWrapper<OrderStat> query = new QueryWrapper<>();
        query.select(groupBy + "," + sumRmbSelect())
                .eq(proxy1 != null, "proxy1", proxy1)
                .eq(proxy2 != null, "proxy2", proxy2)
                .eq(proxy3 != null, "proxy3", proxy3)
                .ge("bet_date", start)
                .le("bet_date", end)
                .groupBy(groupBy);
        return lambdaQuery().getBaseMapper().selectList(query);
    }

    private String sumRmbSelect() {
        return "sum(bet_rmb_amount) bet_rmb_amount, sum(result_rmb_amount) result_rmb_amount, " +
                "sum(valid_amount) valid_amount,sum(valid_rmb_amount) valid_rmb_amount, " +
                "sum(proxy1_rmb_amount) proxy1_rmb_amount, sum(proxy2_rmb_amount) proxy2_rmb_amount, " +
                "sum(proxy3_rmb_amount) proxy3_rmb_amount, sum(backwater_rmb_amount) backwater_rmb_amount, " +
                "sum(bet_count) bet_count";
    }

    private String toPlainString(BigDecimal value) {
        return value.stripTrailingZeros().toPlainString();
    }

    private BigDecimal calcRmb(BigDecimal value, String betCurrency) {
        if (value == null) {
            return BigDecimal.ZERO;
        }
        if (CurrencyEnum.RMB.name().equalsIgnoreCase(betCurrency)) {
            return value;
        }

        BigDecimal rmbRate = currencyService.getRmbRate(betCurrency);
        return value.multiply(rmbRate);
    }
}
