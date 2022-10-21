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
     * @param currency  币种
     * @param allowance 授权额度
     * @param mode      授权额度模式
     * @param fromUserId      额度来源代理id
     * @param mode      额度来源代理授权额度模式
     */
    void updateAllowance(Long userId, BigDecimal allowance, String currency,AllowanceModeEnum mode, Long fromUserId,AllowanceModeEnum fromUserMode);

}
