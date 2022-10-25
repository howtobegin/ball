package com.ball.biz.match.service.impl;

import com.ball.biz.match.entity.Odds;
import com.ball.biz.match.mapper.OddsMapper;
import com.ball.biz.match.service.IOddsService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author lhl
 * @since 2022-10-19
 */
@Slf4j
@Service
public class OddsServiceImpl extends ServiceImpl<OddsMapper, Odds> implements IOddsService {
    @Override
    public Odds queryByBizNo(String bizNo) {
        return lambdaQuery().eq(Odds::getBizNo, bizNo).one();
    }

    @Override
    public List<Odds> queryByMatchId(String matchId) {
        return lambdaQuery().eq(Odds::getMatchId , matchId).list();
    }
}
