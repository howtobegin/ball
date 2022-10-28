package com.ball.biz.account.service;

import com.ball.biz.account.entity.TradeConfig;
import com.ball.biz.account.enums.PlayTypeEnum;
import com.ball.biz.account.enums.SportEnum;
import com.baomidou.mybatisplus.extension.service.IService;

import com.ball.common.service.IBaseService;

import java.util.List;

/**
 * <p>
 * 限额 退水配置 服务类
 * </p>
 *
 * @author atom
 * @since 2022-10-27
 */
public interface ITradeConfigService extends IService<TradeConfig>, IBaseService {

    void init(TradeConfig tradeConfig, Long parentUserNo );

    /**
     * 获取用户限额 退水配置
     * @param userId
     * @return
     */
    List<TradeConfig> getUserConfig(Long userId);

    TradeConfig getUserConfig(Long userId, SportEnum sport, PlayTypeEnum type);

    boolean update(TradeConfig tradeConfig);
}
