package com.ball.biz.match.service.impl;

import com.ball.biz.match.entity.OddsScore;
import com.ball.biz.match.mapper.OddsScoreMapper;
import com.ball.biz.match.service.IOddsScoreService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.common.collect.Lists;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author lhl
 * @since 2022-10-19
 */
@Service
public class OddsScoreServiceImpl extends ServiceImpl<OddsScoreMapper, OddsScore> implements IOddsScoreService {
    @Override
    public OddsScore queryByBizNo(String bizNo) {
        return lambdaQuery().eq(OddsScore::getBizNo, bizNo).one();
    }

    @Override
    public List<OddsScore> queryByMatch(String matchId, Integer type) {
        return lambdaQuery()
                .eq(OddsScore::getMatchId, matchId)
                .eq(type != null, OddsScore::getType, type)
                .list();
    }

    @Override
    public List<OddsScore> queryByMatchId(List<String> matchIds, Integer type, Integer status) {
        if (StringUtils.isEmpty(matchIds)) {
            return Lists.newArrayList();
        }
        return lambdaQuery().in(OddsScore::getMatchId, matchIds)
                .eq(type != null, OddsScore::getType, type)
                .eq(status != null, OddsScore::getStatus, status)
                .list();
    }
}
