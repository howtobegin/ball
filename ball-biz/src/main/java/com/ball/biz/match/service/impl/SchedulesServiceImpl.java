package com.ball.biz.match.service.impl;

import com.ball.biz.bet.enums.ScheduleStatus;
import com.ball.biz.match.entity.Schedules;
import com.ball.biz.match.mapper.SchedulesMapper;
import com.ball.biz.match.service.ISchedulesService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.common.collect.Lists;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.time.LocalDate;
import java.time.LocalDateTime;
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
    @Value("${early.day:14}")
    private int earlyDay;

    @Override
    public Schedules queryOne(String matchId) {
        return lambdaQuery().eq(Schedules::getMatchId , matchId).one();
    }

    @Override
    public List<Schedules> batchQuery(List<String> matchIds) {
        if(CollectionUtils.isEmpty(matchIds)) {
            return Lists.newArrayList();
        }
        return lambdaQuery().in(Schedules::getMatchId, matchIds).list();
    }

    @Override
    public List<Schedules> queryByDate(LocalDateTime start, LocalDateTime end) {
        return lambdaQuery()
                .gt(start != null, Schedules::getMatchDate, start)
                .lt(end != null, Schedules::getMatchDate, end)
                .list();
    }

    @Override
    public List<Schedules> queryInplay(String matchId) {
        return lambdaQuery()
                .eq(!StringUtils.isEmpty(matchId), Schedules::getMatchId, matchId)
                .in(Schedules::getStatus, ScheduleStatus.inplayCodes())
                .orderByAsc(Schedules::getLeagueName)
                .orderByAsc(Schedules::getMatchTime)
                .list();
    }

    @Override
    public List<Schedules> queryToday(List<String> leagueIds, String matchId) {
        return lambdaQuery()
                .eq(!StringUtils.isEmpty(matchId), Schedules::getMatchId, matchId)
                .in(!StringUtils.isEmpty(leagueIds), Schedules::getLeagueId, leagueIds)
                .gt(Schedules::getMatchDate, LocalDate.now())
                .lt(Schedules::getMatchDate, LocalDate.now().plusDays(1))
                .eq(Schedules::getStatus, ScheduleStatus.NOT_STARTED.getCode())
                .orderByAsc(Schedules::getLeagueName)
                .orderByAsc(Schedules::getMatchTime)
                .list();
    }

    @Override
    public List<Schedules> queryEarly(List<String> leagueIds, String matchId) {
        return lambdaQuery()
                .eq(!StringUtils.isEmpty(matchId), Schedules::getMatchId, matchId)
                .in(!StringUtils.isEmpty(leagueIds), Schedules::getLeagueId, leagueIds)
                .gt(Schedules::getMatchDate, LocalDate.now().plusDays(1))
                .lt(Schedules::getMatchDate, LocalDate.now().plusDays(earlyDay + 1))
                .eq(Schedules::getStatus, ScheduleStatus.NOT_STARTED.getCode())
                .orderByAsc(Schedules::getLeagueName)
                .orderByAsc(Schedules::getMatchTime)
                .list();
    }

    @Override
    public List<Schedules> queryByLeagueId(String leagueId) {
        return lambdaQuery().eq(Schedules::getLeagueId, leagueId).list();
    }
}
