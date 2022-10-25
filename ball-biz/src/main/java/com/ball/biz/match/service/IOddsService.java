package com.ball.biz.match.service;

import com.ball.biz.match.entity.Odds;
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
public interface IOddsService extends IService<Odds>, IBaseService {
    /**
     * 根据唯一索引查询
     */
    Odds queryByBizNo(String bizNo);

    List<Odds> queryByMatchId(String matchId);
}
