package com.ball.biz.account.service.impl;

import com.ball.base.transaction.TransactionSupport;
import com.ball.base.util.BizAssert;
import com.ball.biz.account.entity.AssetChangeLog;
import com.ball.biz.account.entity.UserAccount;
import com.ball.biz.account.enums.AccountTransactionType;
import com.ball.biz.account.enums.AllowanceModeEnum;
import com.ball.biz.account.enums.DirectionType;
import com.ball.biz.account.mapper.UserAccountMapper;
import com.ball.biz.account.service.IAssetChangeLogService;
import com.ball.biz.account.service.IAssetFreezeChangeService;
import com.ball.biz.account.service.IUserAccountService;
import com.ball.biz.exception.BizErrCode;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author atom
 * @since 2022-10-18
 */
@Service
public class UserAccountServiceImpl extends ServiceImpl<UserAccountMapper, UserAccount> implements IUserAccountService {

    @Autowired
    private TransactionSupport transactionSupport;

    @Autowired
    private IAssetChangeLogService assetChangeLogService;

    @Autowired
    private IAssetFreezeChangeService assetFreezeChangeService;


    /**
     * 初始化
     *
     * @param userId        用户id
     * @param currency      币种
     * @param userType      用户类型
     * @param allowanceMode 额度模式
     * @param allowance     授权额度
     */
    @Override
    public void init(Long userId, String currency, String userType, AllowanceModeEnum allowanceMode, BigDecimal allowance) {
        save(new UserAccount()
                .setBalance(allowance)
                .setCurrency(currency)
                .setAllowanceMode(allowanceMode.name())
                .setFreezeAmount(BigDecimal.ZERO)
                .setAllowance(allowance)
                .setUserId(userId)
        );
    }

    /**
     * 更新额度
     *
     * @param userId    用户id
     * @param allowance 授权额度
     */
    @Override
    public void updateAllowance(Long userId, BigDecimal allowance) {
        BizAssert.isTrue(allowance.compareTo(BigDecimal.ZERO) >= 0, BizErrCode.DATA_ERROR);
        transactionSupport.execute(()->{
            UserAccount account = query(userId);
            BizAssert.isTrue(account!=null, BizErrCode.ACCOUNT_NOT_EXIST);
            if (account.getAllowanceMode().equalsIgnoreCase(AllowanceModeEnum.BALANCE.name())) {
                //余额模式。直接改动余额
                boolean f = lambdaUpdate().setSql("balance=" + allowance)
                        .eq(UserAccount::getUserId, userId)
                        .eq(UserAccount::getBalance, account.getBalance())
                        .update();
                BizAssert.isTrue(f, BizErrCode.ACCOUNT_CONCURRENT);
            } else if (account.getAllowanceMode().equalsIgnoreCase(AllowanceModeEnum.RECOVERY.name())) {
                //恢复模式。额度和余额同增同减
                BigDecimal newbalance = allowance.subtract(account.getAllowance()).add(account.getBalance());
                BizAssert.isTrue(newbalance.compareTo(BigDecimal.ZERO) >= 0, BizErrCode.ACCOUNT_ALLOWANCE_TOO_LOW,account.getAllowance().subtract(account.getBalance()).stripTrailingZeros().toPlainString());

                //余额模式。直接改动余额
                boolean f = lambdaUpdate().set(UserAccount::getBalance,newbalance)
                        .set(UserAccount::getAllowance, allowance)
                        .eq(UserAccount::getUserId, userId)
                        .eq(UserAccount::getBalance, account.getBalance())
                        .update();
                BizAssert.isTrue(f, BizErrCode.ACCOUNT_CONCURRENT);
            }
        });

    }

    /**
     * 查询账户
     *
     * @param userId 用户id
     * @return
     */
    @Override
    public UserAccount query(Long userId) {
        return lambdaQuery().eq(UserAccount::getUserId, userId).one();
    }

    /**
     * 用户入金
     * @param userId 用户id
     * @param amount 金额
     * @param orderNo 订单号
     * @param transactionType 交易类型
     */
    @Override
    public void income(Long userId, BigDecimal amount,String orderNo, AccountTransactionType transactionType) {
        BizAssert.hasAmount(amount, BizErrCode.CHANGE_AMOUNT_IS_LESS_ZERO);
        transactionSupport.execute(() -> {
            UserAccount account = query(userId);
            BizAssert.isTrue(account!=null, BizErrCode.ACCOUNT_NOT_EXIST);

            String amountStr = amount.stripTrailingZeros().toPlainString();
            boolean f = lambdaUpdate().setSql("balance=balance+" + amountStr)
                    .eq(UserAccount::getUserId, userId)
                    .eq(UserAccount::getBalance, account.getBalance())
                    .update();
            BizAssert.isTrue(f, BizErrCode.ACCOUNT_CONCURRENT);
            // 记录日志
            assetChangeLogService.save(new AssetChangeLog().setAmount(amount)
                    .setFee(BigDecimal.ZERO).setDirect(DirectionType.IN.name())
                    .setOrderNo(orderNo).setTransactionType(transactionType.name())
                    .setUserId(userId).setCurrency(account.getCurrency())
                    .setOldBalance(account.getBalance()).setNewBalance(account.getBalance().add(amount)));
        });
    }

    /**
     * 用户出金
     *
     * @param userId          用户id
     * @param amount          金额
     * @param orderNo         订单号
     * @param transactionType 交易类型
     */
    @Override
    public void payout(Long userId, BigDecimal amount, String orderNo, AccountTransactionType transactionType) {

    }

    /**
     * 冻结
     *
     * @param userId          用户id
     * @param amount          金额
     * @param orderNo         订单号
     * @param fee             手续费
     * @param transactionType 交易类型
     */
    @Override
    public void freeze(Long userId, BigDecimal amount, String orderNo, BigDecimal fee, String transactionType) {

    }

    /**
     * 解冻
     *
     * @param orderNo         订单号
     * @param transactionType 交易类型
     */
    @Override
    public void unfreeze(String orderNo, String transactionType) {

    }

    /**
     * 解冻出金
     *
     * @param orderNo         订单号
     * @param transactionType 交易类型
     */
    @Override
    public void unfreezePayout(String orderNo, String transactionType) {

    }
}
