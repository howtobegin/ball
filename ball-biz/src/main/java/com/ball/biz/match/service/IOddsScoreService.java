package com.ball.biz.match.service;

import com.ball.biz.match.entity.OddsScore;
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
public interface IOddsScoreService extends IService<OddsScore>, IBaseService {
    OddsScore queryByBizNo(String bizNo);

    List<OddsScore> queryByMatch(String matchId, Integer type);

    List<OddsScore> queryByMatchIds(List<String> matchIds, Integer type, Integer status);
}
