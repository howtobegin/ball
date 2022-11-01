package com.ball.biz.account.service;

import com.ball.biz.account.entity.SettlementPeriod;
import com.baomidou.mybatisplus.extension.service.IService;

import com.ball.common.service.IBaseService;

/**
 * <p>
 * 账期 服务类
 * </p>
 *
 * @author atom
 * @since 2022-11-01
 */
public interface ISettlementPeriodService extends IService<SettlementPeriod>, IBaseService {

    /**
     * 当前账期
     * @return
     */
    SettlementPeriod currentPeriod();
}
