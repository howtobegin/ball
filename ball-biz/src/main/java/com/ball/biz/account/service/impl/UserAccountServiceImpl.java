package com.ball.biz.account.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.ball.base.transaction.TransactionSupport;
import com.ball.base.util.BizAssert;
import com.ball.biz.account.entity.AssetChangeLog;
import com.ball.biz.account.entity.AssetFreezeChange;
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
import lombok.extern.slf4j.Slf4j;
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
@Slf4j
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
     */
    @Override
    public void init(Long userId, String currency, String userType, AllowanceModeEnum allowanceMode) {
        save(new UserAccount()
                .setBalance(BigDecimal.ZERO)
                .setCurrency(currency)
                .setAllowanceMode(allowanceMode.name())
                .setFreezeAmount(BigDecimal.ZERO)
                .setAllowance(BigDecimal.ZERO)
                .setUserType(userType)
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
        lambdaUpdate().set(UserAccount::getAllowance, allowance)
                .eq(UserAccount::getUserId, userId)
                .update();

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
        incomeWithCheck(userId, amount, orderNo, transactionType, null);
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
        payoutWithCheck(userId, amount, orderNo, transactionType, null);
    }

    /**
     * 用户入金
     *
     * @param userId          用户id
     * @param amount          金额
     * @param orderNo         订单号
     * @param transactionType 交易类型
     * @param oldBalance      原余额
     */
    @Override
    public void incomeWithCheck(Long userId, BigDecimal amount, String orderNo, AccountTransactionType transactionType, BigDecimal oldBalance) {
        BizAssert.hasAmount(amount, BizErrCode.CHANGE_AMOUNT_IS_LESS_ZERO);
        transactionSupport.execute(() -> {
            String amountStr = amount.stripTrailingZeros().toPlainString();
            if (oldBalance != null) {
                boolean f = lambdaUpdate().setSql("balance=balance+" + amountStr)
                        .eq(UserAccount::getUserId, userId)
                        .eq(UserAccount::getBalance, oldBalance)
                        .update();
                BizAssert.isTrue(f, BizErrCode.ACCOUNT_CONCURRENT);
            } else {
                lambdaUpdate().setSql("balance=balance+" + amountStr)
                        .eq(UserAccount::getUserId, userId)
                        .update();
            }

            // 记录日志
            assetChangeLogService.save(new AssetChangeLog().setAmount(amount)
                    .setFee(BigDecimal.ZERO).setDirect(DirectionType.IN.name())
                    .setOrderNo(orderNo).setTransactionType(transactionType.name())
                    .setUserId(userId));
        });
    }

    /**
     * 用户出金
     *
     * @param userId          用户id
     * @param amount          金额
     * @param orderNo         订单号
     * @param transactionType 交易类型
     * @param oldBalance      原余额
     */
    @Override
    public void payoutWithCheck(Long userId, BigDecimal amount, String orderNo, AccountTransactionType transactionType, BigDecimal oldBalance) {
        BizAssert.hasAmount(amount, BizErrCode.CHANGE_AMOUNT_IS_LESS_ZERO);
        transactionSupport.execute(() -> {

            String amountStr = amount.stripTrailingZeros().toPlainString();
            if (oldBalance != null) {
                boolean f = lambdaUpdate().setSql("balance=balance-" + amountStr)
                        .eq(UserAccount::getUserId, userId)
                        .eq(UserAccount::getBalance, oldBalance)
                        .last(" and (balance-freeze_amount)>=" + amountStr)
                        .update();
                BizAssert.isTrue(f, BizErrCode.ACCOUNT_BALANCE_INSUFFICIENT_OR_CONCURRENT);
            } else {
                boolean f = lambdaUpdate().setSql("balance=balance-" + amountStr)
                        .eq(UserAccount::getUserId, userId)
                        .last(" and (balance-freeze_amount)>=" + amountStr)
                        .update();
                BizAssert.isTrue(f, BizErrCode.ACCOUNT_BALANCE_INSUFFICIENT);
            }

            // 记录日志
            assetChangeLogService.save(new AssetChangeLog().setAmount(amount)
                    .setFee(BigDecimal.ZERO).setDirect(DirectionType.OUT.name())
                    .setOrderNo(orderNo).setTransactionType(transactionType.name())
                    .setUserId(userId));
        });
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
    public void freeze(Long userId, BigDecimal amount, String orderNo, BigDecimal fee, AccountTransactionType transactionType) {
        transactionSupport.execute(() -> {
            UserAccount account = query(userId);
            BizAssert.isTrue(account!=null, BizErrCode.ACCOUNT_NOT_EXIST);
            if (BigDecimal.ZERO.compareTo(amount) >= 0) {
                log.warn("change amount less or equals zero; amount = {}", amount);
                return;
            }
            String amountStr = amount.stripTrailingZeros().toPlainString();
            boolean flag = lambdaUpdate()
                    .setSql("freeze_amount=freeze_amount+" + amountStr)
                    .eq(UserAccount::getUserId, userId)
                    .last(" and (balance-freeze_amount)>=" + amountStr)
                    .update();
            BizAssert.isTrue(flag, BizErrCode.ACCOUNT_BALANCE_INSUFFICIENT);

            assetFreezeChangeService.save(new AssetFreezeChange().setAmount(amount)
                    .setCurrency(account.getCurrency()).setDirect(DirectionType.FREEZE.name())
                    .setFee(fee).setOrderNo(orderNo).setTransactionType(transactionType.name())
                    .setUserId(userId));
        });
    }

    /**
     * 解冻
     *
     * @param orderNo         订单号
     * @param transactionType 交易类型
     */
    @Override
    public void unfreeze(String orderNo, AccountTransactionType transactionType) {
        transactionSupport.execute(() -> {
            AssetFreezeChange assetFreezeChange = assetFreezeChangeService.lambdaQuery().eq(AssetFreezeChange::getOrderNo, orderNo).eq(AssetFreezeChange::getTransactionType, transactionType.name()).one();
            BizAssert.isTrue(assetFreezeChange!=null, BizErrCode.ACCOUNT_FREEZE_NOT_FOUND);

            String amountStr = assetFreezeChange.getAmount().stripTrailingZeros().toPlainString();
            boolean flag = lambdaUpdate()
                    .setSql("freeze_amount=freeze_amount-" + amountStr)
                    .eq(UserAccount::getUserId, assetFreezeChange.getUserId())
                    .last(" and freeze_amount>=" + amountStr)
                    .update();
            BizAssert.isTrue(flag, BizErrCode.ACCOUNT_FREEZE_INSUFFICIENT);
        });
    }

    /**
     * 解冻出金
     *
     * @param orderNo         订单号
     * @param transactionType 交易类型
     */
    @Override
    public void unfreezePayout(String orderNo, AccountTransactionType transactionType) {
        transactionSupport.execute(() -> {
            AssetFreezeChange assetFreezeChange = assetFreezeChangeService.lambdaQuery().eq(AssetFreezeChange::getOrderNo, orderNo).eq(AssetFreezeChange::getTransactionType, transactionType.name()).one();
            BizAssert.isTrue(assetFreezeChange!=null, BizErrCode.ACCOUNT_FREEZE_NOT_FOUND);

            String amountStr = assetFreezeChange.getAmount().stripTrailingZeros().toPlainString();
            boolean flag = lambdaUpdate()
                    .setSql("freeze_amount=freeze_amount-" + amountStr)
                    .setSql("balance=balance-" + amountStr)
                    .eq(UserAccount::getUserId, assetFreezeChange.getUserId())
                    .last(" and freeze_amount>=" + amountStr)
                    .update();
            BizAssert.isTrue(flag, BizErrCode.ACCOUNT_FREEZE_INSUFFICIENT);
        });
    }
}
