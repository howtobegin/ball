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
import com.ball.biz.user.entity.UserInfo;
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
     * @param currency1  币种
     * @param allowance 授权额度
     * @param mode      授权额度模式
     * @param fromUserId      额度来源代理id
     * @param operator      操作者
     */
    @Override
    public void updateAllowance(Long userId, BigDecimal allowance, String currency1,AllowanceModeEnum mode, Long fromUserId,UserInfo operator) {
        BizAssert.isTrue(allowance.compareTo(BigDecimal.ZERO) >= 0, BizErrCode.DATA_ERROR);
        transactionSupport.execute(()->{
            UserAccount userAccount = iUserAccountService.query(userId);
            if (userAccount == null) {
                return;
            }
            AllowanceModeEnum m = mode;
            String currency = currency1;
            if (m == null) {
                m = AllowanceModeEnum.valueOf(userAccount.getAllowanceMode());
            }
            if (currency1 == null) {
                currency = userAccount.getCurrency();
            }
            String orderNo = String.valueOf(snowflake.next());
            BigDecimal rate = iCurrencyService.getRmbRate(currency);
            BigDecimal fromUserAdjustAmount = null;//allowance.multiply(rate).setScale(2,RoundingMode.CEILING);//来源方减少额度
            BizAssetAdjustmentOrder bizAssetAdjustmentOrder = null;
            BigDecimal adjustAmount = null;
            switch (m) {
                case BALANCE://余额模式。直接改动余额

                    adjustAmount = allowance.subtract(userAccount.getBalance().subtract(userAccount.getFreezeAmount()));
                    fromUserAdjustAmount=adjustAmount.negate().multiply(rate);
                    if (fromUserAdjustAmount.compareTo(BigDecimal.ZERO) >= 0) {
                        fromUserAdjustAmount=fromUserAdjustAmount.setScale(0,RoundingMode.CEILING);
                    } else {
                        fromUserAdjustAmount=fromUserAdjustAmount.setScale(0,RoundingMode.FLOOR);
                    }
                    bizAssetAdjustmentOrder = createOrder(userId, adjustAmount,currency,userAccount.getBalance(),allowance,fromUserId,fromUserAdjustAmount, CurrencyEnum.RMB.name(),orderNo,operator);
                    if (adjustAmount.compareTo(BigDecimal.ZERO) >= 0) {
                        iUserAccountService.incomeWithCheck(userId,adjustAmount,orderNo, AccountTransactionType.USER_ADJUSTMENT_IN, userAccount.getBalance());
                    } else {
                        iUserAccountService.payoutWithCheck(userId,adjustAmount.abs(),orderNo, AccountTransactionType.USER_ADJUSTMENT_OUT, userAccount.getBalance());
                    }
                    break;
                case RECOVERY://恢复模式。额度和余额同增同减
                        //修改额度
                    iUserAccountService.updateAllowance(userId, allowance);
                    adjustAmount = allowance.subtract(userAccount.getAllowance());
                    fromUserAdjustAmount=adjustAmount.negate().multiply(rate);
                    if (fromUserAdjustAmount.compareTo(BigDecimal.ZERO) >= 0) {
                        fromUserAdjustAmount=fromUserAdjustAmount.setScale(0,RoundingMode.CEILING);
                    } else {
                        fromUserAdjustAmount=fromUserAdjustAmount.setScale(0,RoundingMode.FLOOR);
                    }
                    bizAssetAdjustmentOrder = createOrder(userId, adjustAmount, currency, userAccount.getBalance(),userAccount.getBalance().add(adjustAmount) ,fromUserId, fromUserAdjustAmount, CurrencyEnum.RMB.name(),orderNo,operator);
                    if (adjustAmount.compareTo(BigDecimal.ZERO) >= 0) {
                        iUserAccountService.income(userId,adjustAmount,orderNo, AccountTransactionType.USER_ADJUSTMENT_IN);
                    } else {
                        iUserAccountService.payout(userId,adjustAmount.abs(),orderNo, AccountTransactionType.USER_ADJUSTMENT_OUT);
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
            if (fromUserAdjustAmount.compareTo(BigDecimal.ZERO) >= 0) {
                iUserAccountService.incomeWithCheck(fromUserId,fromUserAdjustAmount,orderNo, AccountTransactionType.USER_ADJUSTMENT_IN, agent.getBalance());

            } else {
                iUserAccountService.payoutWithCheck(fromUserId,fromUserAdjustAmount.abs(),orderNo, AccountTransactionType.AGENT_ADJUSTMENT_OUT, agent.getBalance());

            }

        });

    }

    /**
     * 更新额度
     *
     * @param userId     用户id
     * @param allowance  授权额度
     * @param fromUserId 额度来源代理id
     * @param operator      操作者
     */
    @Override
    public void updateAllowance(Long userId, BigDecimal allowance, Long fromUserId,UserInfo operator) {
        updateAllowance(userId, allowance,  null, null, fromUserId,operator);
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
    private BizAssetAdjustmentOrder createOrder(Long userId, BigDecimal amount, String currency, BigDecimal oldBalance, BigDecimal newBalance, Long fromUserId, BigDecimal fromUserAmount, String fromUserCurrency, String orderNo, UserInfo operator) {
        BizAssetAdjustmentOrder order = new BizAssetAdjustmentOrder().setAmount(amount).setUserNo(userId).setCurrency(currency)
                .setOldBalance(oldBalance).setNewBalance(newBalance)
                .setFromUserNo(fromUserId).setFromUserAmount(fromUserAmount).setFromUserCurrency(fromUserCurrency)
                .setOrderNo(orderNo)
                .setOperatorId(operator.getId())
                .setOperatorName(operator.getAccount());

        save(order);
        return order;
    }
}
