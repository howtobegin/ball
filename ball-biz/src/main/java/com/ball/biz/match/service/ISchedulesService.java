package com.ball.biz.match.service;

import com.ball.biz.match.entity.Schedules;
import com.baomidou.mybatisplus.extension.service.IService;

import com.ball.common.service.IBaseService;

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
}
