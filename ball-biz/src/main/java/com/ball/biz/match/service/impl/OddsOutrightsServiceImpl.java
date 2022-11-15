package com.ball.biz.match.service.impl;

import com.ball.biz.bet.enums.OddsOutrightsType;
import com.ball.biz.match.entity.OddsOutrights;
import com.ball.biz.match.mapper.OddsOutrightsMapper;
import com.ball.biz.match.service.IOddsOutrightsService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author lhl
 * @since 2022-11-15
 */
@Service
public class OddsOutrightsServiceImpl extends ServiceImpl<OddsOutrightsMapper, OddsOutrights> implements IOddsOutrightsService {
    @Override
    public List<OddsOutrights> queryByLeagueAndType(String leagueId, OddsOutrightsType type) {
        return lambdaQuery().eq(OddsOutrights::getLeagueId , leagueId)
                .eq(type != null, OddsOutrights::getTypeId, type.getCode())
                .list();
    }

    @Override
    public List<OddsOutrights> queryByLeagueAndType(String leagueId, List<Integer> types) {
        return lambdaQuery().eq(OddsOutrights::getLeagueId , leagueId)
                .in(!CollectionUtils.isEmpty(types), OddsOutrights::getTypeId, types)
                .list();
    }
}
