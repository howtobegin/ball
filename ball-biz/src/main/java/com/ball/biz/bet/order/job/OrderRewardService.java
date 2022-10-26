package com.ball.biz.bet.order.job;

import com.ball.base.transaction.TransactionSupport;
import com.ball.biz.account.enums.AccountTransactionType;
import com.ball.biz.account.service.IUserAccountService;
import com.ball.biz.bet.enums.BetResult;
import com.ball.biz.bet.enums.OrderStatus;
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
public class OrderRewardService extends BaseJobService<OrderInfo> {
    @Autowired
    private IOrderInfoService orderInfoService;
    @Autowired
    private IUserAccountService userAccountService;
    @Autowired
    private TransactionSupport transactionSupport;

    @Value("${order.match.reward.page.size:100}")
    private int pageSize;

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
        // userAdd = 5
        // win,
        BigDecimal userAdd = calcResult.getResultAmount().subtract(calcResult.getBetAmount());

        // TODO 根据占成，计算代理123收入/支出，正负区分

        transactionSupport.execute(()->{
            // 用户增加或减少
            if (userAdd.compareTo(BigDecimal.ZERO) > 0) {
                // 解冻或出金
                userAccountService.unfreeze(data.getOrderId(), AccountTransactionType.TRADE);
                userAccountService.income(userId, userAdd, orderId, AccountTransactionType.TRADE);
                // 代理123，出
                //userAccountService.payout(null, userAdd, orderId, AccountTransactionType.TRADE);
            } else if (userAdd.compareTo(BigDecimal.ZERO) == 0) {

                // 解冻或出金
                userAccountService.unfreezePayout(data.getOrderId(), AccountTransactionType.TRADE);


                // 代理123进
            } else {

            }
            // 平台账户减少或增加
        });
        return true;
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
