package com.ball.biz.account.service.impl;

import com.ball.base.transaction.TransactionSupport;
import com.ball.base.util.BizAssert;
import com.ball.biz.account.entity.BizAssetAdjustmentOrder;
import com.ball.biz.account.entity.UserAccount;
import com.ball.biz.account.enums.AccountTransactionType;
import com.ball.biz.account.enums.AllowanceModeEnum;
import com.ball.biz.account.mapper.BizAssetAdjustmentOrderMapper;
import com.ball.biz.account.service.IBizAssetAdjustmentOrderService;
import com.ball.biz.account.service.ICurrencyService;
import com.ball.biz.account.service.IUserAccountService;
import com.ball.biz.enums.CurrencyEnum;
import com.ball.biz.exception.BizErrCode;
import com.ball.common.service.Snowflake;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * <p>
 * 调账订单 服务实现类
 * </p>
 *
 * @author atom
 * @since 2022-10-20
 */
@Service
public class BizAssetAdjustmentOrderServiceImpl extends ServiceImpl<BizAssetAdjustmentOrderMapper, BizAssetAdjustmentOrder> implements IBizAssetAdjustmentOrderService {

    @Autowired
    private TransactionSupport transactionSupport;

    @Autowired
    private IUserAccountService iUserAccountService;

    @Autowired
    private ICurrencyService iCurrencyService;

    @Autowired
    Snowflake snowflake;

//    @Autowired

    /**
     * 更新额度
     *
     * @param userId    用户id
     * @param currency  币种
     * @param allowance 授权额度
     * @param mode      授权额度模式
     * @param fromUserId      额度来源代理id
     */
    @Override
    public void updateAllowance(Long userId, BigDecimal allowance, String currency,AllowanceModeEnum mode, Long fromUserId) {
        BizAssert.isTrue(allowance.compareTo(BigDecimal.ZERO) >= 0, BizErrCode.DATA_ERROR);
        transactionSupport.execute(()->{
            UserAccount userAccount = iUserAccountService.query(userId);
            if (userAccount == null) {
                return;
            }
            String orderNo = String.valueOf(snowflake.next());
            BigDecimal rate = iCurrencyService.getRmbRate(currency);
            BigDecimal fromUserAdjustAmount = allowance.multiply(rate).setScale(2,RoundingMode.CEILING);//来源方减少额度
            BizAssetAdjustmentOrder bizAssetAdjustmentOrder = null;
            switch (mode) {
                case BALANCE://余额模式。直接改动余额

                        BigDecimal adjustAmount = allowance.subtract(userAccount.getBalance());
                        bizAssetAdjustmentOrder = createOrder(userId, adjustAmount,currency,userAccount.getBalance(),allowance,fromUserId,fromUserAdjustAmount, CurrencyEnum.RMB.name(),orderNo);
                        if (adjustAmount.compareTo(BigDecimal.ZERO) >= 0) {
                            iUserAccountService.incomeWithCheck(userId,adjustAmount,orderNo, AccountTransactionType.USER_ADJUSTMENT_IN, userAccount.getBalance());
                        } else {
                            iUserAccountService.payoutWithCheck(userId,adjustAmount.abs(),orderNo, AccountTransactionType.USER_ADJUSTMENT_OUT, userAccount.getBalance());
                        }
                    break;
                case RECOVERY://恢复模式。额度和余额同增同减
                        //修改额度
                        iUserAccountService.updateAllowance(userId, allowance);
                        BigDecimal adjustAmount1 = allowance.subtract(userAccount.getAllowance());
                        bizAssetAdjustmentOrder = createOrder(userId, adjustAmount1, currency, userAccount.getBalance(),userAccount.getBalance().add(adjustAmount1) ,fromUserId, fromUserAdjustAmount, CurrencyEnum.RMB.name(),orderNo);
                        if (adjustAmount1.compareTo(BigDecimal.ZERO) >= 0) {
                            iUserAccountService.income(userId,adjustAmount1,orderNo, AccountTransactionType.USER_ADJUSTMENT_IN);
                        } else {
                            iUserAccountService.payout(userId,adjustAmount1.abs(),orderNo, AccountTransactionType.USER_ADJUSTMENT_OUT);
                        }

                    break;
            }

            if (fromUserId == null) {
                return;
            }

            UserAccount agent = iUserAccountService.query(fromUserId);
            if (agent == null) {
                return;
            }
            iUserAccountService.payoutWithCheck(fromUserId,fromUserAdjustAmount,orderNo, AccountTransactionType.AGENT_ADJUSTMENT_OUT, agent.getBalance());

        });

    }

    /**
     * 创建一个调账业务订单
     * @param userId
     * @param amount
     * @param currency
     * @param fromUserId
     * @param fromUserAmount
     * @param fromUserCurrency
     * @param orderNo
     * @return
     */
    private BizAssetAdjustmentOrder createOrder(Long userId, BigDecimal amount, String currency,BigDecimal oldBalance,BigDecimal newBalance,Long fromUserId, BigDecimal fromUserAmount,String fromUserCurrency,String orderNo) {
        BizAssetAdjustmentOrder order = new BizAssetAdjustmentOrder().setAmount(amount).setUserNo(userId).setCurrency(currency)
                .setOldBalance(oldBalance).setNewBalance(newBalance)
                .setFromUserNo(fromUserId).setFromUserAmount(fromUserAmount).setFromUserCurrency(fromUserCurrency).setOrderNo(orderNo);

        save(order);
        return order;
    }
}
