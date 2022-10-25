package com.ball.biz.match.service.impl;

import com.ball.biz.match.entity.Schedules;
import com.ball.biz.match.mapper.SchedulesMapper;
import com.ball.biz.match.service.ISchedulesService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Date;
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
public class SchedulesServiceImpl extends ServiceImpl<SchedulesMapper, Schedules> implements ISchedulesService {
    @Override
    public Schedules queryOne(String matchId) {
        return lambdaQuery().eq(Schedules::getMatchId , matchId).one();
    }

    @Override
    public List<Schedules> queryByDate(LocalDateTime start, LocalDateTime end) {
        return lambdaQuery()
                .gt(start != null, Schedules::getMatchDate, start)
                .lt(end != null, Schedules::getMatchDate, end)
                .list();
    }
}
