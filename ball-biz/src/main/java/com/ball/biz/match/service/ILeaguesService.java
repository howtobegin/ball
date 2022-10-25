package com.ball.biz.match.service;

import com.ball.biz.match.entity.Leagues;
import com.ball.common.service.IBaseService;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author lhl
 * @since 2022-10-19
 */
public interface ILeaguesService extends IService<Leagues>, IBaseService {
    List<Leagues> query(List<String> leagueIds);
}
