package com.ball.biz.match.service;

import com.ball.biz.match.entity.Favorite;
import com.baomidou.mybatisplus.extension.service.IService;

import com.ball.common.service.IBaseService;

import java.util.List;

/**
 * <p>
 * 收藏夹表 服务类
 * </p>
 *
 * @author lhl
 * @since 2022-10-28
 */
public interface IFavoriteService extends IService<Favorite>, IBaseService {

    Favorite queryOne(Long userId, String matchId);

    List<Favorite> queryList(Long userId);

    void like(Long userId, Integer type, String leagueId, String matchId);

    void unLike(Long userId, String matchId);
}
