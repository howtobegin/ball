package com.ball.biz.match.service;

import com.ball.biz.bet.enums.OddsOutrightsType;
import com.ball.biz.match.entity.OddsOutrights;
import com.ball.common.service.IBaseService;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author lhl
 * @since 2022-11-15
 */
public interface IOddsOutrightsService extends IService<OddsOutrights>, IBaseService {

    List<OddsOutrights> queryByLeagueAndType(String leagueId, OddsOutrightsType type);

    List<OddsOutrights> queryByLeagueAndType(String leagueId, List<Integer> types);
}
