package com.ball.biz.account.service;

import com.ball.biz.account.entity.BizAssetAdjustmentOrder;
import com.ball.biz.account.enums.AllowanceModeEnum;
import com.baomidou.mybatisplus.extension.service.IService;

import com.ball.common.service.IBaseService;

import java.math.BigDecimal;

/**
 * <p>
 * 调账订单 服务类
 * </p>
 *
 * @author atom
 * @since 2022-10-20
 */
public interface IBizAssetAdjustmentOrderService extends IService<BizAssetAdjustmentOrder>, IBaseService {

    /**
     * 更新额度
     *
     * @param userId    用户id
     * @param allowance 授权额度
     * @param mode      授权额度模式
     */
    void updateAllowance(Long userId, BigDecimal allowance, AllowanceModeEnum mode);

    /**
     * 创建一个调账业务订单
     * @param userId
     * @param amount
     */
    BizAssetAdjustmentOrder createOrder(Long userId, BigDecimal amount);
}
