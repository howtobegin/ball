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
import com.ball.biz.order.bo.OrderFinishBo;
import com.ball.biz.order.entity.OrderInfo;
import com.ball.biz.order.service.IOrderInfoService;
import com.ball.biz.user.bo.ProxyRateInfo;
import com.ball.biz.user.proxy.ProxyUserService;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

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
    @Autowired
    private ProxyUserService proxyUserService;

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
        // 用户输赢
        BigDecimal userWinAmount = calcResult.getResultAmount().subtract(calcResult.getBetAmount());
        // 计算代理占成
        ProxyAmount proxyAmount = calcProxyAmount(userId, userWinAmount);
        OrderFinishBo orderFinishBo = buildOrderFinishBo(orderId, calcResult, proxyAmount);
        transactionSupport.execute(()->{
            // 解冻
            userAccountService.unfreeze(data.getOrderId(), AccountTransactionType.TRADE);

            // 用户赢，代理出
            if (userWinAmount.compareTo(BigDecimal.ZERO) > 0) {
                userAccountService.income(userId, userWinAmount, orderId, AccountTransactionType.TRADE);
            }
            // 用户不输不赢
            else if (userWinAmount.compareTo(BigDecimal.ZERO) == 0) {
            }
            // 用户输
            else {
                BigDecimal userLose = BigDecimal.ZERO.subtract(userWinAmount);
                // 用户出
                userAccountService.payout(userId, userLose, orderId, AccountTransactionType.TRADE);
            }
            // 订单完成，更新对应数据
            orderInfoService.finish(orderFinishBo);

        });
        return true;
    }

    private OrderFinishBo buildOrderFinishBo(String orderId, CalcResult calcResult, ProxyAmount proxyAmount) {
        // 赢或输.abs()
        BigDecimal validAmount = BigDecimal.ZERO;
        if (calcResult.getResult() == BetResult.WIN || calcResult.getResult() == BetResult.WIN_HALF) {
            validAmount = calcResult.getResultAmount();
        } else if (calcResult.getResult() == BetResult.LOSE || calcResult.getResult() == BetResult.LOSE_HALF) {
            validAmount = calcResult.getResultAmount().subtract(calcResult.getBetAmount()).abs();
        }

        return OrderFinishBo.builder()
                .orderId(orderId)
                .pre(OrderStatus.SETTLED)
                .resultAmount(calcResult.getResultAmount())
                .validAmount(validAmount)
                .proxy1Amount(proxyAmount.getProxy1Amount())
                .proxy2Amount(proxyAmount.getProxy2Amount())
                .proxy3Amount(proxyAmount.getProxy3Amount())
                .build();
    }

    /**
     * @param amount    用户赢或输的金额
     * @return
     */
    private ProxyAmount calcProxyAmount(Long userId, BigDecimal amount) {
        ProxyRateInfo proxyRateInfo = proxyUserService.getProxyRateByUid(userId);
        log.info("userId {} amount {} proxyRateInfo {}", userId, amount, JSON.toJSON(proxyRateInfo));

        // 占成百分比
        BigDecimal proxy1Percent = Optional.ofNullable(proxyRateInfo).map(ProxyRateInfo::getProxyOneRate).orElse(BigDecimal.ZERO);
        BigDecimal proxy2Percent = Optional.ofNullable(proxyRateInfo).map(ProxyRateInfo::getProxyTwoRate).orElse(BigDecimal.ZERO);
        BigDecimal proxy3Percent = Optional.ofNullable(proxyRateInfo).map(ProxyRateInfo::getProxyThreeRate).orElse(BigDecimal.ZERO);

        // 符号去反
        BigDecimal proxy1Amount = BigDecimal.ZERO.subtract(amount.multiply(proxy1Percent));
        BigDecimal proxy2Amount = BigDecimal.ZERO.subtract(amount.multiply(proxy2Percent));
        BigDecimal proxy3Amount = BigDecimal.ZERO.subtract(amount.multiply(proxy3Percent));
        log.info("amount {} p1 {} p2 {} p3 {}", amount, proxy1Amount, proxy2Amount, proxy3Amount);
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
