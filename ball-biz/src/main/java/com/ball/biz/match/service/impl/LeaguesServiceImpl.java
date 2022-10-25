package com.ball.biz.match.service.impl;

import com.ball.biz.match.entity.Leagues;
import com.ball.biz.match.mapper.LeaguesMapper;
import com.ball.biz.match.service.ILeaguesService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.common.collect.Lists;
import org.apache.commons.collections4.CollectionUtils;
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
@Service
public class LeaguesServiceImpl extends ServiceImpl<LeaguesMapper, Leagues> implements ILeaguesService {
    @Override
    public List<Leagues> query(List<String> leagueIds) {
        if (CollectionUtils.isEmpty(leagueIds)) {
            return Lists.newArrayList();
        }
        return lambdaQuery()
                .in(Leagues::getLeagueId, leagueIds)
                .list();
    }
}
