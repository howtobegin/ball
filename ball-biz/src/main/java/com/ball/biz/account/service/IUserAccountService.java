package com.ball.biz.account.service;

import com.ball.biz.account.entity.UserAccount;
import com.ball.biz.account.enums.AccountTransactionType;
import com.ball.biz.account.enums.AllowanceModeEnum;
import com.baomidou.mybatisplus.extension.service.IService;

import com.ball.common.service.IBaseService;

import java.math.BigDecimal;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author atom
 * @since 2022-10-18
 */
public interface IUserAccountService extends IService<UserAccount>, IBaseService {

    /**
     * 初始化
     * @param userId 用户id
     * @param currency  币种
     * @param userType  用户类型
     * @param allowanceMode  额度模式
     * @param allowance  授权额度
     */
    void init(Long userId, String currency, String userType, AllowanceModeEnum allowanceMode, BigDecimal allowance);

    /**
     * 更新额度
     * @param userId 用户id
     * @param allowance 授权额度
     */
    void updateAllowance(Long userId, BigDecimal allowance);

    /**
     * 查询账户
     * @param userId 用户id
     * @return
     */
    UserAccount query(Long userId);

    /**
     * 用户入金
     * @param userId 用户id
     * @param amount 金额
     * @param orderNo 订单号
     * @param transactionType 交易类型
     */
    void income(Long userId, BigDecimal amount,String orderNo, AccountTransactionType transactionType);


    /**
     * 用户出金
     * @param userId 用户id
     * @param amount 金额
     * @param orderNo 订单号
     * @param transactionType 交易类型
     */
    void payout(Long userId, BigDecimal amount,String orderNo, AccountTransactionType transactionType);

    /**
     * 冻结
     * @param userId 用户id
     * @param amount 金额
     * @param orderNo 订单号
     * @param fee 手续费
     * @param transactionType 交易类型
     */
    void freeze(Long userId, BigDecimal amount, String orderNo, BigDecimal fee, String transactionType);

    /**
     * 解冻
     * @param orderNo 订单号
     * @param transactionType 交易类型
     */
    void unfreeze(String orderNo, String transactionType);

    /**
     * 解冻出金
     * @param orderNo 订单号
     * @param transactionType 交易类型
     */
    void unfreezePayout(String orderNo, String transactionType);

}
