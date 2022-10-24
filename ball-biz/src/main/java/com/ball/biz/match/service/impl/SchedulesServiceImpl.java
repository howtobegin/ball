package com.ball.biz.match.service.impl;

import com.ball.biz.match.entity.Schedules;
import com.ball.biz.match.mapper.SchedulesMapper;
import com.ball.biz.match.service.ISchedulesService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author lhl
 * @since 2022-10-19
 */
@Service
public class SchedulesServiceImpl extends ServiceImpl<SchedulesMapper, Schedules> implements ISchedulesService {
    @Override
    public Schedules queryOne(String matchId) {
        return lambdaQuery().eq(Schedules::getMatchId , matchId).one();
    }
}
