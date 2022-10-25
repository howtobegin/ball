package com.ball.biz.match.service;

import com.ball.biz.match.entity.Schedules;
import com.ball.common.service.IBaseService;
import com.baomidou.mybatisplus.extension.service.IService;

import java.time.LocalDateTime;
import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author lhl
 * @since 2022-10-19
 */
public interface ISchedulesService extends IService<Schedules>, IBaseService {
    Schedules queryOne(String matchId);

    List<Schedules> queryByDate(LocalDateTime start, LocalDateTime end);

    /**
     * 进行中的
     * @return
     */
    List<Schedules> queryInplay(String matchId);

    /**
     * 今日，未开始的
     */
    List<Schedules> queryToday(List<String> leagueIds, String matchId);

    /**
     * 早盘
     * @return
     */
    List<Schedules> queryEarly(List<String> leagueIds, String matchId);
}
