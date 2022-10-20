package com.ball.biz.account.service.impl;

import com.ball.base.transaction.TransactionSupport;
import com.ball.base.util.BizAssert;
import com.ball.biz.account.entity.BizAssetAdjustmentOrder;
import com.ball.biz.account.entity.UserAccount;
import com.ball.biz.account.enums.AccountTransactionType;
import com.ball.biz.account.enums.AllowanceModeEnum;
import com.ball.biz.account.mapper.BizAssetAdjustmentOrderMapper;
import com.ball.biz.account.service.IBizAssetAdjustmentOrderService;
import com.ball.biz.account.service.IUserAccountService;
import com.ball.biz.exception.BizErrCode;
import com.ball.common.service.Snowflake;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

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
    Snowflake snowflake;

//    @Autowired

    /**
     * 更新额度
     *
     * @param userId    用户id
     * @param allowance 授权额度
     * @param mode      授权额度模式
     */
    @Override
    public void updateAllowance(Long userId, BigDecimal allowance, AllowanceModeEnum mode) {
        BizAssert.isTrue(allowance.compareTo(BigDecimal.ZERO) >= 0, BizErrCode.DATA_ERROR);
        switch (mode) {
            case BALANCE://余额模式。直接改动余额
                transactionSupport.execute(()->{
                    UserAccount userAccount = iUserAccountService.query(userId);
                    if (userAccount == null) {
                        return;
                    }
                    BigDecimal adjustAmount = allowance.subtract(userAccount.getBalance());
                    BizAssetAdjustmentOrder bizAssetAdjustmentOrder = createOrder(userId, adjustAmount);
                    if (adjustAmount.compareTo(BigDecimal.ZERO) >= 0) {
                        iUserAccountService.incomeWithCheck(userId,adjustAmount,bizAssetAdjustmentOrder.getOrderNo(), AccountTransactionType.ADJUSTMENT, userAccount.getBalance());
                    } else {
                        iUserAccountService.payoutWithCheck(userId,adjustAmount.abs(),bizAssetAdjustmentOrder.getOrderNo(), AccountTransactionType.ADJUSTMENT, userAccount.getBalance());
                    }

                });


                break;
            case RECOVERY://恢复模式。额度和余额同增同减
                transactionSupport.execute(()->{
                    UserAccount userAccount = iUserAccountService.query(userId);
                    if (userAccount == null) {
                        return;
                    }
                    //修改额度
                    iUserAccountService.updateAllowance(userId, allowance);
                    BigDecimal adjustAmount = allowance.subtract(userAccount.getAllowance());
                    BizAssetAdjustmentOrder bizAssetAdjustmentOrder = createOrder(userId, adjustAmount);
                    if (adjustAmount.compareTo(BigDecimal.ZERO) >= 0) {
                        iUserAccountService.income(userId,adjustAmount,bizAssetAdjustmentOrder.getOrderNo(), AccountTransactionType.ADJUSTMENT);
                    } else {
                        iUserAccountService.payout(userId,adjustAmount.abs(),bizAssetAdjustmentOrder.getOrderNo(), AccountTransactionType.ADJUSTMENT);
                    }

                });
                break;
        }

    }

    /**
     * 创建一个调账业务订单
     *
     * @param userId
     * @param amount
     */
    @Override
    public BizAssetAdjustmentOrder createOrder(Long userId, BigDecimal amount) {
        BizAssetAdjustmentOrder order = new BizAssetAdjustmentOrder().setAmount(amount).setUserNo(userId).setOrderNo(String.valueOf(snowflake.next()));

        save(order);
        return order;
    }
}
