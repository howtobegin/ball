package com.ball.app.service;

import com.ball.app.controller.match.vo.CountryLeagueResp;
import com.ball.app.controller.match.vo.LeagueReq;
import com.ball.app.controller.match.vo.LeagueResp;
import com.ball.base.util.BeanUtil;
import com.ball.biz.match.entity.Leagues;
import com.ball.biz.match.entity.Schedules;
import com.ball.biz.match.service.ILeaguesService;
import com.ball.biz.match.service.ISchedulesService;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author lhl
 * @date 2022/10/31 下午6:50
 */
@Slf4j
@Component
public class BizLeagueService {
    @Autowired
    private ILeaguesService leaguesService;
    @Autowired
    private ISchedulesService schedulesService;

    public List<CountryLeagueResp> list(LeagueReq req) {
        List<Schedules> schedules;
        if (req.getType() == 2) {
            schedules = schedulesService.queryToday(null, null);
        } else if (req.getType() == 3) {
            schedules = schedulesService.queryEarly(null, null);
        } else {
            return Lists.newArrayList();
        }
        List<String> leagueIds = schedules.stream().map(Schedules::getLeagueId).distinct().collect(Collectors.toList());
        return groupByCount(leaguesService.query(leagueIds));
    }


    public List<CountryLeagueResp> groupByCount(List<Leagues> list) {
        List<CountryLeagueResp> ret = Lists.newArrayList();
        Map<String, List<LeagueResp>> map = list.stream()
                .map(l -> BeanUtil.copy(l, LeagueResp.class))
                .collect(Collectors.groupingBy(LeagueResp::getCountry));
        map.forEach((k, v)->{
            ret.add(CountryLeagueResp.builder()
                    .country(k)
                    .leagueResp(v)
                    .build());
        });
        return ret;
    }
}
