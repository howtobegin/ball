package com.ball.biz.account.service.impl;

import com.ball.biz.account.entity.SettlementPeriod;
import com.ball.biz.account.mapper.SettlementPeriodMapper;
import com.ball.biz.account.service.ISettlementPeriodService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 账期 服务实现类
 * </p>
 *
 * @author atom
 * @since 2022-11-01
 */
@Service
public class SettlementPeriodServiceImpl extends ServiceImpl<SettlementPeriodMapper, SettlementPeriod> implements ISettlementPeriodService {
    /**
     * 当前账期
     *
     * @return
     */
    @Override
    public SettlementPeriod currentPeriod() {
        return lambdaQuery().last("limit 1").orderByDesc(SettlementPeriod::getId).one();
    }
}
