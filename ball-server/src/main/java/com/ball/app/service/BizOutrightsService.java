package com.ball.app.service;

import com.ball.app.controller.match.vo.OutrightsGroupOddsResp;
import com.ball.app.controller.match.vo.OutrightsGroupResp;
import com.ball.app.controller.match.vo.OutrightsOddsResp;
import com.ball.app.controller.match.vo.OutrightsWinnerResp;
import com.ball.biz.bet.enums.OddsOutrightsType;
import com.ball.biz.match.entity.OddsOutrights;
import com.ball.biz.match.entity.Schedules;
import com.ball.biz.match.service.IOddsOutrightsService;
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
 * @date 2022/11/15 下午5:51
 */
@Slf4j
@Component
public class BizOutrightsService {
    private static final String LEAGUE_ID = "1572";

    @Autowired
    private ISchedulesService schedulesService;
    @Autowired
    private IOddsOutrightsService oddsOutrightsService;

    /**
     * 获取世界杯比赛
     * @return
     */
    public List<Schedules> getSchedules() {
        return schedulesService.queryByLeagueId(LEAGUE_ID);
    }

    /**
     * 小组
     * @return
     */
    public List<OutrightsGroupResp> groupList() {
        List<OddsOutrights> list = oddsOutrightsService.queryByLeagueAndType(LEAGUE_ID, OddsOutrightsType.groupTypes());
        // 按小组分
        Map<String, List<OddsOutrights>> typeToOdds = list.stream().collect(Collectors.groupingBy(o -> OddsOutrightsType.parse(o.getTypeId()).getGroup()));

        List<OutrightsGroupResp> ret = Lists.newArrayList();

        typeToOdds.forEach((type, oddsList) -> {
            Map<String, List<OddsOutrights>> itemToOdds = oddsList.stream().collect(Collectors.groupingBy(OddsOutrights::getItem));

            OutrightsGroupResp resp = OutrightsGroupResp.builder().build();
            resp.setGroup(type);

            List<OutrightsGroupOddsResp> oddsRespList = Lists.newArrayList();
            itemToOdds.forEach((item, subOdds) -> {
                OddsOutrights one = subOdds.get(0);
                OutrightsGroupOddsResp r = translate(one);
                for (OddsOutrights o : subOdds) {
                    if (o.getTypeId() >= 6 && o.getTypeId() <= 13) {
                        r.setWinnerOdds(OutrightsOddsResp.builder()
                                .bizNo(String.valueOf(o.getBizNo()))
                                .odds(o.getOdds())
                                .build());
                    } else {
                        r.setQualifiedOdds(OutrightsOddsResp.builder()
                                .bizNo(String.valueOf(o.getBizNo()))
                                .odds(o.getOdds())
                                .build());
                    }
                }
                oddsRespList.add(r);
            });
            resp.setOddsList(oddsRespList);
            ret.add(resp);
        });
        return ret;
    }

    private OutrightsGroupOddsResp translate(OddsOutrights one) {
        return OutrightsGroupOddsResp.builder()
                .leagueNameZh(one.getLeagueNameZh())
                .itemId(one.getItemId())
                .itemZh(one.getItemZh())
                .drawn(0)
                .gamesPlayed(0)
                .goalsAgainst(0)
                .goalsDifference(0)
                .goalsScored(0)
                .lose(0)
                .points(0)
                .win(0)
                .build();
    }

    /**
     * 冠军
     * @return
     */
    public List<OutrightsWinnerResp> winnerList() {
        List<OddsOutrights> list = oddsOutrightsService.queryByLeagueAndType(LEAGUE_ID, OddsOutrightsType.OUTRIGHT);
        return list.stream().map(this::translateToOutrightsWinnerResp).collect(Collectors.toList());
    }

    private OutrightsWinnerResp translateToOutrightsWinnerResp(OddsOutrights o) {
        return OutrightsWinnerResp.builder()
                .bizNo(String.valueOf(o.getBizNo()))
                .itemId(o.getItemId())
                .itemZh(o.getItemZh())
                .leagueNameZh(o.getLeagueNameZh())
                .odds(o.getOdds())
                .build();
    }
}
