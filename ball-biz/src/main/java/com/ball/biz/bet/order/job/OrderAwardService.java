package com.ball.biz.bet.order.job;

import com.alibaba.fastjson.JSON;
import com.ball.base.transaction.TransactionSupport;
import com.ball.biz.account.enums.AccountTransactionType;
import com.ball.biz.account.service.IUserAccountService;
import com.ball.biz.bet.enums.BetResult;
import com.ball.biz.bet.enums.OrderStatus;
import com.ball.biz.bet.order.bo.ProxyAmount;
import com.ball.biz.bet.order.calculate.CalculatorHolder;
import com.ball.biz.bet.order.calculate.bo.CalcResult;
import com.ball.biz.order.entity.OrderInfo;
import com.ball.biz.order.service.IOrderInfoService;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;

/**
 * 派奖，账户变动
 *
 * @author lhl
 * @date 2022/10/20 下午6:39
 */
@Slf4j
@Getter
@Component
public class OrderAwardService extends BaseJobService<OrderInfo> {
    @Autowired
    private IOrderInfoService orderInfoService;
    @Autowired
    private IUserAccountService userAccountService;
    @Autowired
    private TransactionSupport transactionSupport;

    @Value("${order.match.reward.page.size:100}")
    private int pageSize;

    private static final BigDecimal ONE_HUNDRED = BigDecimal.TEN.multiply(BigDecimal.TEN);

    @Override
    public boolean executeOne(OrderInfo data) {
        log.info("orderId {}", data.getOrderId());
        BetResult betResult = BetResult.parse(data.getBetResult());
        if (betResult == null) {
            log.warn("orderId {} betResult {}", data.getOrderId(), betResult);
            return false;
        }
        CalcResult calcResult = CalculatorHolder.get(betResult).calc(data.getBetAmount(), data.getBetOdds());
        String orderId = data.getOrderId();
        Long userId = data.getUserId();
        // betAmount 10, resultAmount = 15;
        // userWinAmount = 5
        // win,
        BigDecimal userWinAmount = calcResult.getResultAmount().subtract(calcResult.getBetAmount());

        transactionSupport.execute(()->{
            // 解冻
            userAccountService.unfreeze(data.getOrderId(), AccountTransactionType.TRADE);

            ProxyAmount proxyAmount = null;
            // 用户赢，代理出
            if (userWinAmount.compareTo(BigDecimal.ZERO) > 0) {
                userAccountService.income(userId, userWinAmount, orderId, AccountTransactionType.TRADE);
                proxyAmount = calcProxyAmount(userWinAmount, true);
            }
            // 用户不输不赢
            else if (userWinAmount.compareTo(BigDecimal.ZERO) == 0) {
                // 解冻即可
                proxyAmount = calcProxyAmount(userWinAmount, false);
            }
            // 用户输
            else {
                BigDecimal userLose = BigDecimal.ZERO.subtract(userWinAmount);
                // 用户出
                userAccountService.payout(userId, userLose, orderId, AccountTransactionType.TRADE);
                proxyAmount = calcProxyAmount(userWinAmount, false);
            }
            // 记录代理占成，支出用负数表示
            orderInfoService.updateProxyAmount(orderId, proxyAmount.getProxy1Amount(), proxyAmount.getProxy2Amount(), proxyAmount.getProxy3Amount());

        });
        return true;
    }

    private ProxyAmount calcProxyAmount(BigDecimal amount, boolean payout) {
        // TODO 真实占成
        BigDecimal proxy1Percent = BigDecimal.valueOf(3L);
        BigDecimal proxy2Percent = BigDecimal.valueOf(3L);
        BigDecimal proxy3Percent = BigDecimal.valueOf(4L);

        BigDecimal proxy1Amount = amount.multiply(proxy1Percent).divide(ONE_HUNDRED, 2, BigDecimal.ROUND_DOWN);
        BigDecimal proxy2Amount = amount.multiply(proxy2Percent).divide(ONE_HUNDRED, 2, BigDecimal.ROUND_DOWN);
        BigDecimal proxy3Amount = amount.subtract(proxy1Amount).subtract(proxy2Amount);

        if (payout) {
            proxy1Amount = BigDecimal.ZERO.subtract(proxy1Amount);
            proxy2Amount = BigDecimal.ZERO.subtract(proxy2Amount);
            proxy3Amount = BigDecimal.ZERO.subtract(proxy3Amount);
        }

        return ProxyAmount.builder()
                .amount(amount)
                .proxy1Percent(proxy1Percent)
                .proxy2Percent(proxy2Percent)
                .proxy3Percent(proxy3Percent)
                .proxy1Amount(proxy1Amount)
                .proxy2Amount(proxy2Amount)
                .proxy3Amount(proxy3Amount)
                .build();
    }

    /**
     *
     * @return
     */
    @Override
    public List<OrderInfo> fetchData() {
        log.info("maxCallbackId {} pageSize {}", maxCallbackId, pageSize);
        return orderInfoService.lambdaQuery()
                .gt(OrderInfo::getId, maxCallbackId)
                // 比赛半场完，完成，取消，中断
                .eq(OrderInfo::getStatus, OrderStatus.SETTLED.getCode())
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
