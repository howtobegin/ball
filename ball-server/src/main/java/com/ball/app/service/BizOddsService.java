package com.ball.app.service;

import com.ball.app.controller.match.vo.*;
import com.ball.base.context.UserContext;
import com.ball.base.util.BeanUtil;
import com.ball.base.util.BizAssert;
import com.ball.biz.bet.enums.HandicapType;
import com.ball.biz.bet.enums.MatchTimeType;
import com.ball.biz.bet.enums.ScheduleStatus;
import com.ball.biz.bet.order.OrderHelper;
import com.ball.biz.bet.processor.assist.OddsAssist;
import com.ball.biz.exception.BizErrCode;
import com.ball.biz.match.entity.*;
import com.ball.biz.match.service.*;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author lhl
 * @date 2022/10/25 上午11:51
 */
@Slf4j
@Component
public class BizOddsService {
    @Autowired
    private ISchedulesService schedulesService;
    @Autowired
    private ILeaguesService leaguesService;
    @Autowired
    private IOddsService oddsService;
    @Autowired
    private IOddsScoreService oddsScoreService;
    @Autowired
    private IFavoriteService favoriteService;

    public List<HandicapMatchOddsResp> oddsList(Integer type, List<String> leagueIds, String matchId) {
        BizAssert.notEmpty(leagueIds, BizErrCode.PARAM_ERROR_DESC,"leagueIds");
        List<Schedules> schedules = Lists.newArrayList();
        if (type == 2){
            // 2 今日
            schedules = schedulesService.queryToday(leagueIds, matchId);
        } else if (type == 3) {
            // 3 早盘
            schedules = schedulesService.queryEarly(leagueIds, matchId);
        }
        return mainOddsList(schedules, 1);
    }
    /**
     * 首页，滚球数据
     * @return
     */
    public List<HandicapMatchOddsResp> handicapOddsList(String matchId) {
        List<Schedules> schedules = schedulesService.queryInplay(matchId);
        return mainOddsList(schedules, 2);
    }

    /**
     *
     * @param schedules
     * @param oddsScoreStatus   波胆用：1 今日和早盘;2 滚盘
     * @return
     */
    public List<HandicapMatchOddsResp> mainOddsList(List<Schedules> schedules, Integer oddsScoreStatus) {
        List<Favorite> favorites = favoriteService.queryList(UserContext.getUserNo());
        Set<String> matchIds = favorites.stream().map(Favorite::getMatchId).collect(Collectors.toSet());
        return mainOddsList(schedules, oddsScoreStatus, matchIds);
    }
    public List<HandicapMatchOddsResp> mainOddsList(List<Schedules> schedules, Integer oddsScoreStatus, Set<String> favoriteMatchIds) {
        log.info("start schedules size {}",schedules.size());
        List<HandicapMatchOddsResp> ret = Lists.newArrayList();
        // 联赛ID
        List<String> leagueIds = schedules.stream().map(Schedules::getLeagueId).distinct().collect(Collectors.toList());
        log.info("leagueIds size {}", leagueIds.size());
        List<Leagues> leagues = leaguesService.query(leagueIds);
        log.info("leagues's size {}", leagues.size());
        Map<String, Leagues> leagueMap = leagues.stream().collect(Collectors.toMap(Leagues::getLeagueId, Function.identity()));
        Map<String, Schedules> matchSchedules = schedules.stream().collect(Collectors.toMap(Schedules::getMatchId, Function.identity()));

        List<String> matchIds = schedules.stream().map(Schedules::getMatchId).distinct().collect(Collectors.toList());
        List<Odds> odds = oddsService.queryByMatchId(matchIds);
        log.info("odds size {}", odds.size());
        // <比赛ID, List<赔率>>
        Map<String, List<Odds>> matchOdds = odds.stream().collect(Collectors.groupingBy(Odds::getMatchId));
        // 波胆
        List<OddsScore> oddsScores = oddsScoreService.queryByMatchIds(matchIds, null, oddsScoreStatus);
        log.info("oddsScores size {}", oddsScores.size());
        // <比赛ID， List<波胆>>
        Map<String, List<OddsScore>> matchOddsScore = oddsScores.stream().collect(Collectors.groupingBy(OddsScore::getMatchId));
        // <联赛ID, List<比赛ID>>
        Map<String, List<String>> leagueMatchIds = schedules.stream().collect(Collectors.toMap(Schedules::getLeagueId, s -> {
            List<String> v = Lists.newArrayList();
            v.add(s.getMatchId());
            return v;
        }, (List<String> v1, List<String> v2) -> {
            v1.addAll(v2);
            return v1;
        }));
        leagueMatchIds.forEach((leagueId, matchIdOfLeague) -> {
            LeagueResp leagueResp = translate(leagueMap.get(leagueId));

            List<MatchOddsResp> matchOddsRespList = Lists.newArrayList();
            List<MatchOddsScoreResp> matchOddsScoreRespList = Lists.newArrayList();
            for (String matchId : matchIdOfLeague) {
                List<Odds> matchOfOdds = matchOdds.get(matchId);
                if(matchOfOdds == null) {
                    continue;
                }
                MatchResp matchResp = translate(matchSchedules.get(matchId), leagueResp.getNameZh());
                setMatchOtherInfo(matchResp, favoriteMatchIds);

                // 主要玩儿法，全场
                Map<HandicapType, List<OddsResp>> fullOdds = Maps.newHashMap();
                // 主要玩儿法，半场
                Map<HandicapType, List<OddsResp>> halfOdds = Maps.newHashMap();
                for (Odds odd : matchOfOdds) {
                    HandicapType type = HandicapType.parse(odd.getType());
                    OddsResp oddsResp = BeanUtil.copy(odd, OddsResp.class);
                    oddsResp.setInstantHandicap(OrderHelper.translate(oddsResp.getInstantHandicap()));
                    oddsResp.setInstantHandicapDesc(oddsResp.getInstantHandicap());
                    if (HandicapType.fullTypes(MatchTimeType.FULL).contains(odd.getType())) {
                        addToMap(fullOdds, type, oddsResp);
                    } else if (showHalf(matchResp)){
                        // 半场没有返回close，通过比赛状态判定
                        judgeClose(matchResp, oddsResp);
                        addToMap(halfOdds, type, oddsResp);
                    }
                }

                // 波胆全场和半场
                List<OddsScore> matchOfOddsScore = matchOddsScore.get(matchId);
                Map<HandicapType, List<OddsScoreResp>> oddsScore = groupOddsScore(matchOfOddsScore);

                matchOddsRespList.add(MatchOddsResp.builder()
                        .count(matchOfOdds.size())
                        .match(matchResp)
                        .odds(fullOdds)
                        .halfOdds(halfOdds)
                        .oddsScore(oddsScore)
                        .build());
                matchOddsScoreRespList.add(MatchOddsScoreResp.builder()
                        .match(matchResp)
                        .odds(oddsScore)
                        .build());
            }
            ret.add(HandicapMatchOddsResp.builder()
                    .league(leagueResp)
                    .matchOddsResp(matchOddsRespList)
                    .matchOddsScoreResp(matchOddsScoreRespList)
                    .build());
        });
        log.info("end");
        return ret;
    }

    private boolean showHalf(MatchResp match) {
        return ScheduleStatus.NOT_STARTED.isMe(match.getStatus())
                || ScheduleStatus.FIRST_HALF.isMe(match.getStatus());
    }

    private void judgeClose(MatchResp matchResp, OddsResp resp) {
        HandicapType type = HandicapType.parse(resp.getType());
        resp.setIsClose(OddsAssist.rejudgmentClose(resp.getIsClose(), matchResp.getStatus(), type, resp.getOddsType()));
    }

    private LeagueResp translate(Leagues league) {
        LeagueResp leagueResp = BeanUtil.copy(league, LeagueResp.class);
        if (leagueResp != null) {
            leagueResp.setName(leagueResp.getNameZh());
        }
        return leagueResp;
    }

    private MatchResp translate(Schedules schedule, String leagueName) {
        MatchResp matchResp = BeanUtil.copy(schedule, MatchResp.class);
        if (matchResp != null) {
            matchResp.setHomeName(matchResp.getHomeNameZh());
            matchResp.setAwayName(matchResp.getAwayNameZh());
            matchResp.setLeagueName(leagueName);
        }
        return matchResp;
    }

    private void setMatchOtherInfo(MatchResp matchResp, Set<String> favoriteMatchIds) {
        ScheduleStatus status = ScheduleStatus.parse(matchResp.getStatus());
        matchResp.setStatusDesc(Optional.ofNullable(status).map(ScheduleStatus::getDesc).orElse(String.valueOf(matchResp.getStatus())));
        matchResp.setFavorite(favoriteMatchIds.contains(matchResp.getMatchId()));
    }

    private Map<HandicapType, List<OddsScoreResp>> groupOddsScore(List<OddsScore> matchOfOddsScore) {
        Map<HandicapType, List<OddsScoreResp>> oddsScore = Maps.newHashMap();
        if (matchOfOddsScore != null) {
            Map<Integer, List<OddsScoreResp>> tmp = matchOfOddsScore.stream()
                    .map(o -> {
                        OddsScoreResp resp = BeanUtil.copy(o, OddsScoreResp.class);
                        resp.setBizNo(o.getBizNo()+"");
                        return resp;
                    })
                    .collect(Collectors.groupingBy(OddsScoreResp::getType));
            oddsScore.put(HandicapType.CORRECT_SCORE, tmp.get(1));
            oddsScore.put(HandicapType.CORRECT_SCORE_HALL, tmp.get(2));
        }
        return oddsScore;
    }

    private void addToMap(Map<HandicapType, List<OddsResp>> map, HandicapType type, OddsResp resp) {
        List<OddsResp> list = map.get(type);
        if (list == null) {
            list = Lists.newArrayList();
            map.putIfAbsent(type, list);
        }
        list.add(resp);
    }

    /**
     * 查某个比赛赔率
     * @return
     */
    public MatchOddsResp matchOddsList(String matchId) {
        Schedules schedules = schedulesService.queryOne(matchId);
        boolean inplay = ScheduleStatus.inplayCodes().contains(schedules.getStatus());
        Integer oddsScoreStatus = inplay ? 2 : 1;
        List<Schedules> list = Lists.newArrayList(schedules);
        List<HandicapMatchOddsResp> handicapMatchOddsResps = mainOddsList(list, oddsScoreStatus);
        if (!CollectionUtils.isEmpty(handicapMatchOddsResps)) {
            List<MatchOddsResp> matchOddsRespList = handicapMatchOddsResps.get(0).getMatchOddsResp();
            return !CollectionUtils.isEmpty(matchOddsRespList) ? matchOddsRespList.get(0) : null;
        }
        return null;
    }

    public List<HandicapMatchOddsResp> favoriteList(Long userId) {
        List<Favorite> favorites = favoriteService.queryList(userId);
        List<String> matchIds = favorites.stream().map(Favorite::getMatchId).collect(Collectors.toList());
        List<Schedules> schedules = schedulesService.batchQuery(matchIds);

        return mainOddsList(schedules, 1, Sets.newHashSet(matchIds));
    }
}
