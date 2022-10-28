package com.ball.biz.account.service.impl;

import com.ball.base.util.BizAssert;
import com.ball.biz.account.entity.TradeConfig;
import com.ball.biz.account.enums.PlayTypeEnum;
import com.ball.biz.account.enums.SportEnum;
import com.ball.biz.account.enums.UserLevelEnum;
import com.ball.biz.account.mapper.TradeConfigMapper;
import com.ball.biz.account.service.ITradeConfigService;
import com.ball.biz.exception.BizErrCode;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

/**
 * <p>
 * 限额 退水配置 服务实现类
 * </p>
 *
 * @author atom
 * @since 2022-10-27
 */
@Service
public class TradeConfigServiceImpl extends ServiceImpl<TradeConfigMapper, TradeConfig> implements ITradeConfigService {
    @Override
    public void init(TradeConfig tradeConfig, Long parentUserNo) {
        TradeConfig parent = null;
        if (parentUserNo != null) {
            parent = lambdaQuery().eq(TradeConfig::getUserNo, parentUserNo)
                    .eq(TradeConfig::getType,tradeConfig.getType())
                    .eq(TradeConfig::getSport, tradeConfig.getSport()).one();
        }
        if (parent != null) {
            _check(tradeConfig, parent);
        }

        save(tradeConfig);

    }

    @Override
    public boolean update(TradeConfig tradeConfig) {
        return updateById(tradeConfig);
    }

    /**
     * 获取用户限额 退水配置
     *
     * @param userId
     * @return
     */
    @Override
    public List<TradeConfig> getUserConfig(Long userId) {
        return lambdaQuery().eq(TradeConfig::getUserNo, userId).list();
    }

    @Override
    public TradeConfig getUserConfig(Long userId, SportEnum sport, PlayTypeEnum type) {
        return lambdaQuery().eq(TradeConfig::getUserNo, userId)
                .eq(TradeConfig::getSport, sport.name())
                .eq(TradeConfig::getType, type.name()).one();
    }

    private void _check(TradeConfig tradeConfig, TradeConfig parent) {
        if (tradeConfig.getUserLevel() == null) {
            if (tradeConfig.getType().equalsIgnoreCase(PlayTypeEnum.HOE.name()) || tradeConfig.getType().equalsIgnoreCase(PlayTypeEnum.HOE_INPAY.name())) {
                BizAssert.isTrue(tradeConfig.getA().compareTo(parent.getA()) <= 0, BizErrCode.TRADE_CONFIG_INLLEGAL);
                BizAssert.isTrue(tradeConfig.getB().compareTo(parent.getB()) <= 0, BizErrCode.TRADE_CONFIG_INLLEGAL);
                BizAssert.isTrue(tradeConfig.getC().compareTo(parent.getC()) <= 0, BizErrCode.TRADE_CONFIG_INLLEGAL);
                BizAssert.isTrue(tradeConfig.getD().compareTo(parent.getD()) <= 0, BizErrCode.TRADE_CONFIG_INLLEGAL);
            } else {
                BizAssert.isTrue(tradeConfig.getMatchLimit().compareTo(parent.getMatchLimit()) <= 0, BizErrCode.TRADE_CONFIG_INLLEGAL);
                BizAssert.isTrue(tradeConfig.getOrderLimit().compareTo(parent.getOrderLimit()) <= 0, BizErrCode.TRADE_CONFIG_INLLEGAL);

            }

        } else {
            if (tradeConfig.getType().equalsIgnoreCase(PlayTypeEnum.HOE.name()) || tradeConfig.getType().equalsIgnoreCase(PlayTypeEnum.HOE_INPAY.name())) {
                UserLevelEnum levelEnum = UserLevelEnum.valueOf(tradeConfig.getUserLevel());
                switch (levelEnum) {
                    case A:
                        BizAssert.isTrue(tradeConfig.getA().compareTo(parent.getA()) <= 0, BizErrCode.TRADE_CONFIG_INLLEGAL);
                        break;
                    case B:
                        BizAssert.isTrue(tradeConfig.getB().compareTo(parent.getB()) <= 0, BizErrCode.TRADE_CONFIG_INLLEGAL);
                        break;
                    case C:
                        BizAssert.isTrue(tradeConfig.getC().compareTo(parent.getC()) <= 0, BizErrCode.TRADE_CONFIG_INLLEGAL);
                        break;
                    case D:
                        BizAssert.isTrue(tradeConfig.getD().compareTo(parent.getD()) <= 0, BizErrCode.TRADE_CONFIG_INLLEGAL);
                        break;
                }
            } else {
                BizAssert.isTrue(tradeConfig.getMatchLimit().compareTo(parent.getMatchLimit()) <= 0, BizErrCode.TRADE_CONFIG_INLLEGAL);
                BizAssert.isTrue(tradeConfig.getOrderLimit().compareTo(parent.getOrderLimit()) <= 0, BizErrCode.TRADE_CONFIG_INLLEGAL);

            }

        }


    }

    @Override
    public BigDecimal getUserRate(TradeConfig tradeConfig) {
        UserLevelEnum levelEnum = UserLevelEnum.valueOf(tradeConfig.getUserLevel());
        switch (levelEnum) {
            case A:
                return tradeConfig.getA();
            case B:
                return tradeConfig.getB();
            case C:
                return tradeConfig.getC();
            case D:
                return tradeConfig.getD();
        }
        return null;
    }
}
