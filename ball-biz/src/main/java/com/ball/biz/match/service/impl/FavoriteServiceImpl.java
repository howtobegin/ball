package com.ball.biz.match.service.impl;

import com.ball.biz.match.entity.Favorite;
import com.ball.biz.match.mapper.FavoriteMapper;
import com.ball.biz.match.service.IFavoriteService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 收藏夹表 服务实现类
 * </p>
 *
 * @author lhl
 * @since 2022-10-28
 */
@Service
public class FavoriteServiceImpl extends ServiceImpl<FavoriteMapper, Favorite> implements IFavoriteService {
    @Override
    public Favorite queryOne(Long userId, String matchId) {
        return lambdaQuery()
                .eq(Favorite::getUserId, userId)
                .eq(Favorite::getMatchId, matchId)
                .one();
    }

    @Override
    public List<Favorite> queryList(Long userId) {
        return lambdaQuery()
                .eq(Favorite::getUserId, userId)
                .list();
    }

    @Override
    public void like(Long userId, Integer type, String leagueId, String matchId) {
        Favorite exists = queryOne(userId, matchId);
        if (exists != null) {
            return;
        }
        Favorite favorite = new Favorite()
                .setUserId(userId)
                .setLeagueId(leagueId)
                .setMatchId(matchId)
                .setFavoriteType(type);
        save(favorite);
    }

    @Override
    public void unLike(Long userId, String matchId) {
        Favorite exists = queryOne(userId, matchId);
        if (exists == null) {
            return;
        }
        removeById(exists.getId());
    }
}
