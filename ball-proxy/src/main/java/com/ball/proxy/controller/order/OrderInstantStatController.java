package com.ball.proxy.controller.order;

import com.ball.base.context.UserContext;
import com.ball.base.util.BeanUtil;
import com.ball.base.util.BizAssert;
import com.ball.biz.account.entity.SettlementPeriod;
import com.ball.biz.account.service.ISettlementPeriodService;
import com.ball.biz.bet.enums.OddsType;
import com.ball.biz.bet.enums.Sport;
import com.ball.biz.enums.UserTypeEnum;
import com.ball.biz.exception.BizErrCode;
import com.ball.biz.match.entity.Leagues;
import com.ball.biz.match.entity.Schedules;
import com.ball.biz.match.service.ILeaguesService;
import com.ball.biz.match.service.ISchedulesService;
import com.ball.biz.order.entity.OrderInfo;
import com.ball.biz.order.service.IOrderInfoService;
import com.ball.proxy.controller.order.vo.instant.*;
import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.google.common.collect.Lists;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.eclipse.jetty.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author lhl
 * @date 2022/11/1 下午2:58
 */
@Api(tags = "订单即时统计相关")
@RestController
@RequestMapping("/proxy/order/instant/stat")
public class OrderInstantStatController {

    @Autowired
    IOrderInfoService orderInfoService;

    @Autowired
    ILeaguesService iLeaguesService;

    @Autowired
    ISchedulesService iSchedulesService;

    @Autowired
    ISettlementPeriodService settlementPeriodService;

    @ApiOperation("总览")
    @PostMapping(value = "overview" )
    public OverviewResp overview(@RequestBody @Valid OverviewReq req){
        Integer userType = UserContext.getUserType();
        SettlementPeriod period = settlementPeriodService.currentPeriod();
        BizAssert.notNull(period,BizErrCode.ACCOUNT_PERIOD_NOT_FOUND);
        List<OrderInfo> all = null;
        if (userType == UserTypeEnum.PROXY_ONE.v) {
            all = orderInfoService.lambdaQuery().eq(OrderInfo::getProxy1, UserContext.getUserNo()).ge(OrderInfo::getCreateTime, period.getStartDate()).list();
        } else if (userType == UserTypeEnum.PROXY_TWO.v) {
            all = orderInfoService.lambdaQuery().eq(OrderInfo::getProxy2, UserContext.getUserNo()).ge(OrderInfo::getCreateTime, period.getStartDate()).list();

        } else if (userType == UserTypeEnum.PROXY_THREE.v) {
            all = orderInfoService.lambdaQuery().eq(OrderInfo::getProxy3, UserContext.getUserNo()).ge(OrderInfo::getCreateTime, period.getStartDate()).list();
        }

        OverviewResp resp = new OverviewResp();

        Map<Integer,OverviewDetailResp> map = all.stream().collect(Collectors.groupingBy(OrderInfo::getSport)).entrySet().stream().map(e->{
            OverviewDetailResp detail = new OverviewDetailResp();
            detail.setSport(e.getKey());
            //@ApiModelProperty("滚球：投注笔数")
            Long inplayBetCount = 0L;

            //@ApiModelProperty("滚球：投注数量")
            BigDecimal inplayBetAmount = BigDecimal.ZERO;

            //@ApiModelProperty("今日：投注笔数")
            Long todayBetCount = 0L;

            //@ApiModelProperty("今日：投注数量")
            BigDecimal todayBetAmount = BigDecimal.ZERO;

            //@ApiModelProperty("早盘：投注笔数")
            Long earlyBetCount = 0L;

            //@ApiModelProperty("早盘：投注数量")
            BigDecimal earlyBetAmount = BigDecimal.ZERO;

            for (OrderInfo o: e.getValue()) {
                if (o.getOddsType() == null) {
                    continue;
                }
                OddsType oddsType = OddsType.parse(o.getOddsType());
                switch (oddsType) {
                    case IN_PLAY_ODDS:
                        inplayBetCount++;

                        inplayBetAmount = inplayBetAmount.add(_calTypeAmount(o,req.getDataType()));
                        break;
                    case NONE:
                    case EARLY_ODDS:
                        earlyBetCount++;
                        earlyBetAmount = earlyBetAmount.add(_calTypeAmount(o,req.getDataType()));
                        break;
                    case INSTANT_ODDS:
                        todayBetCount++;
                        todayBetAmount = todayBetAmount.add(_calTypeAmount(o,req.getDataType()));
                        break;

                }

            }
            detail.setInplayBetCount(inplayBetCount);
            detail.setInplayBetAmount(inplayBetAmount);
            detail.setEarlyBetCount(earlyBetCount);
            detail.setEarlyBetAmount(earlyBetAmount);
            detail.setTodayBetCount(todayBetCount);
            detail.setTodayBetAmount(todayBetAmount);
            return detail;
        }).collect(Collectors.toMap(OverviewDetailResp::getSport, OverviewDetailResp->OverviewDetailResp));
        OverviewDetailResp total = new OverviewDetailResp();
        total.setSport(0);
        total.setInplayBetCount(0L);
        total.setInplayBetAmount(BigDecimal.ZERO);
        total.setTodayBetCount(0L);
        total.setTodayBetAmount(BigDecimal.ZERO);
        total.setEarlyBetCount(0L);
        total.setEarlyBetAmount(BigDecimal.ZERO);
//        detailRespList.forEach(d->{
//            total.setInplayBetCount(total.getInplayBetCount()+d.getInplayBetCount());
//            total.setInplayBetAmount(total.getInplayBetAmount().add(d.getInplayBetAmount()));
//            total.setTodayBetCount(total.getTodayBetCount()+d.getTodayBetCount());
//            total.setTodayBetAmount(total.getTodayBetAmount().add(d.getTodayBetAmount()));
//            total.setEarlyBetCount(total.getEarlyBetCount()+d.getEarlyBetCount());
//            total.setEarlyBetAmount(total.getEarlyBetAmount().add(d.getEarlyBetAmount()));
//        });
        List<OverviewDetailResp> list = _initList();
        list.forEach(d->{
            OverviewDetailResp v = map.get(d.getSport());
            if (v != null) {
                d.setInplayBetCount(v.getInplayBetCount());
                d.setInplayBetAmount(v.getInplayBetAmount());
                d.setTodayBetCount(v.getTodayBetCount());
                d.setTodayBetAmount(v.getTodayBetAmount());
                d.setEarlyBetCount(v.getEarlyBetCount());
                d.setEarlyBetAmount(v.getEarlyBetAmount());

                total.setInplayBetCount(total.getInplayBetCount()+d.getInplayBetCount());
                total.setInplayBetAmount(total.getInplayBetAmount().add(d.getInplayBetAmount()));
                total.setTodayBetCount(total.getTodayBetCount()+d.getTodayBetCount());
                total.setTodayBetAmount(total.getTodayBetAmount().add(d.getTodayBetAmount()));
                total.setEarlyBetCount(total.getEarlyBetCount()+d.getEarlyBetCount());
                total.setEarlyBetAmount(total.getEarlyBetAmount().add(d.getEarlyBetAmount()));
            }

        });

        resp.setDetails(list);

        resp.setBetAmount(total.getEarlyBetAmount().add(total.getTodayBetAmount()).add(total.getInplayBetAmount()));
        resp.setBetCount(total.getEarlyBetCount()+total.getTodayBetCount()+total.getInplayBetCount());

        return resp;
    }

    private List<OverviewDetailResp> _initList() {
        List<OverviewDetailResp> res = new ArrayList<>(Sport.values().length);
        for (Sport s: Sport.values()) {
            OverviewDetailResp d = new OverviewDetailResp();
            d.setSport(s.getCode());
            d.setInplayBetCount(0L);
            d.setInplayBetAmount(BigDecimal.ZERO);
            d.setTodayBetCount(0L);
            d.setTodayBetAmount(BigDecimal.ZERO);
            d.setEarlyBetCount(0L);
            d.setEarlyBetAmount(BigDecimal.ZERO);
            res.add(d);

        }
        return res;

    }

    private BigDecimal _calTypeAmount(OrderInfo order, Integer type) {
        BigDecimal res = BigDecimal.ZERO;
        //观看类型：1 所有；2 我的占成, 3 代理商占成，4 总代理+ 代理商占成
        if (type == 1) {
            return order.getBetRmbAmount();
        } else if (type == 2) {
            if (UserContext.getUserNo().equals(order.getProxy2())) {
                return order.getProxy2Amount();
            } else if (UserContext.getUserNo().equals(order.getProxy3())) {
                return order.getProxy3Amount();
            }  else if (UserContext.getUserNo().equals(order.getProxy1())) {
                return order.getProxy1Amount();
            }
        } else if (type == 3 ) {
            if  (UserContext.getUserNo().equals(order.getProxy1())) {
                return order.getProxy3Amount().add(order.getProxy2Amount());
            } else if (UserContext.getUserNo().equals(order.getProxy2())) {
                return order.getProxy3Amount();
            }
        } else if (type == 4) {
            if  (UserContext.getUserNo().equals(order.getProxy1())) {
                return order.getProxy1Amount().add(order.getProxy3Amount()).add(order.getProxy2Amount());
            } else if (UserContext.getUserNo().equals(order.getProxy2())) {
                return order.getProxy3Amount().add(order.getProxy2Amount());
            } else if (UserContext.getUserNo().equals(order.getProxy3())) {
                return order.getProxy3Amount();
            }
        }

        return BigDecimal.ZERO;
    }


    @ApiOperation("实时比赛")
    @PostMapping(value = "realtimeMatch" )
    public RealtimeMatchResp realtimeMatch(@RequestBody @Valid RealtimeMatchReq req){

        List<OrderInfo> all = _queryAlOrder(req);
        Set<String> leagueIds = new HashSet<>();
        Set<String> matchIds = new HashSet<>();

        List<LeagueMatchResp> leagues = all.stream().filter(o->(o.getLeagueId()!=null && o.getMatchId()!=null)).collect(Collectors.groupingBy(OrderInfo::getLeagueId)).entrySet().stream().map(e->{
            LeagueMatchResp leagueMatchResp = new LeagueMatchResp();
            LeagueResp league = new LeagueResp();
            league.setLeagueId(e.getKey());
            leagueIds.add(e.getKey());
            leagueMatchResp.setLeague(league);
            leagueMatchResp.setBetCount(0L);
            leagueMatchResp.setBetAmount(BigDecimal.ZERO);
            leagueMatchResp.setId(e.getKey());

            leagueMatchResp.setMatchList(e.getValue().stream().collect(Collectors.groupingBy(OrderInfo::getMatchId)).entrySet().stream().map(m->{
                MatchResp match = new MatchResp();
                match.setMatchId(m.getKey());
                matchIds.add(m.getKey());
                match.setBetAmount(BigDecimal.ZERO);
                match.setBetCount(0L);
                m.getValue().forEach(o->{
                    match.setBetAmount(match.getBetAmount().add(o.getBetRmbAmount()));
                    match.setBetCount(match.getBetCount() + 1);
                });
                leagueMatchResp.setBetAmount(leagueMatchResp.getBetAmount().add(match.getBetAmount()));
                leagueMatchResp.setBetCount(leagueMatchResp.getBetCount()+match.getBetCount());
                return match;
            }).collect(Collectors.toList()));


            return leagueMatchResp;
        }).collect(Collectors.toList());

        _fillLeagueAndMatchInfo(leagueIds,matchIds,leagues);
        RealtimeMatchResp res = new RealtimeMatchResp();
        res.setLeagues(leagues);
        res.setBetCount(leagues.stream().mapToLong(LeagueMatchResp::getBetCount).sum());
        res.setBetAmount(leagues.stream().map(LeagueMatchResp::getBetAmount).reduce(BigDecimal.ZERO, BigDecimal::add));
        return res;
    }

    private List<OrderInfo> _queryAlOrder(RealtimeMatchReq req){
        SettlementPeriod period = settlementPeriodService.currentPeriod();
        BizAssert.notNull(period,BizErrCode.ACCOUNT_PERIOD_NOT_FOUND);

        LambdaQueryChainWrapper<OrderInfo> wrapper =orderInfoService.lambdaQuery()
                .ge(OrderInfo::getCreateTime, period.getStartDate()).eq(OrderInfo::getSport, req.getSport())
                .eq(OrderInfo::getOddsType, req.getOddsType());

        if (StringUtil.isNotBlank(req.getLeagueIds())) {
            String[] ll = req.getLeagueIds().split(",");
            wrapper.in(OrderInfo::getLeagueId,Lists.newArrayList(ll));
        }

        if (req.getBetAmount() != null && req.getBetAmount().compareTo(BigDecimal.ZERO) > 0) {
            wrapper.ge(OrderInfo::getBetRmbAmount, req.getBetAmount());
        }

        Integer userType = UserContext.getUserType();
        if (userType == UserTypeEnum.PROXY_ONE.v) {
            wrapper.eq(OrderInfo::getProxy1, UserContext.getUserNo());
        } else if (userType == UserTypeEnum.PROXY_TWO.v) {
            wrapper.eq(OrderInfo::getProxy2, UserContext.getUserNo());

        } else if (userType == UserTypeEnum.PROXY_THREE.v) {
            wrapper.eq(OrderInfo::getProxy3, UserContext.getUserNo());
        }

        if (StringUtil.isNotBlank(req.getUserIds())) {
            String[] uu = req.getUserIds().split(",");
            wrapper.in(OrderInfo::getUserId,Lists.newArrayList(uu));

        }

        return wrapper.list();
    }

    private void _fillLeagueAndMatchInfo(Set<String> leagueIds, Set<String> matchIds, List<LeagueMatchResp> result) {
        if (leagueIds.size()==0 || matchIds.size() == 0) {
            return;
        }
        List<Leagues> leaguesList = iLeaguesService.query(new ArrayList<>(leagueIds));
        Map<String,Leagues> leagueMap = leaguesList.stream().collect(Collectors.toMap(Leagues::getLeagueId,Leagues->Leagues));
        List<Schedules> matchList = iSchedulesService.lambdaQuery().in(Schedules::getMatchId, new ArrayList<>(matchIds)).list();
        Map<String,Schedules> schedulesMap =  matchList.stream().collect(Collectors.toMap(Schedules::getMatchId,Schedules->Schedules));

        result.forEach(r->{
            Leagues l = leagueMap.get(r.getLeague().getLeagueId());
            if (l!=null){
                r.getLeague().setName(l.getName());
                r.getLeague().setNameZh(l.getNameZh());
            }
            r.getMatchList().forEach(m->{
                Schedules s = schedulesMap.get(m.getMatchId());
                if (s!= null ) {
                    m.setHomeName(s.getHomeName());
                    m.setHomeNameZh(s.getHomeNameZh());
                    m.setAwayName(s.getAwayName());
                    m.setAwayNameZh(s.getAwayNameZh());
                    m.setHomeScore(s.getHomeScore());
                    m.setHomeHalfScore(s.getHomeHalfScore());
                    m.setAwayScore(s.getAwayScore());
                    m.setAwayHalfScore(s.getAwayHalfScore());
                    m.setMatchDate(s.getMatchDate());
                }
            });
        });
    }

//    @ApiOperation("滚球")
//    @PostMapping(value = "inplay" )
//    public List<LeagueMatchResp> inplay(@RequestBody @Valid TodayReq req){
//        return Lists.newArrayList();
//    }
//
//    @ApiOperation("今日")
//    @PostMapping(value = "today" )
//    public List<LeagueMatchResp> today(@RequestBody @Valid TodayReq req){
//        return Lists.newArrayList();
//    }
//
//    @ApiOperation("早盘")
//    @PostMapping(value = "early" )
//    public List<LeagueMatchResp> early(@RequestBody @Valid EarlyReq req){
//        return Lists.newArrayList();
//    }
//
//    @ApiOperation("已开赛")
//    @PostMapping(value = "started" )
//    public List<LeagueMatchResp> started(@RequestBody @Valid StartedReq req){
//        return Lists.newArrayList();
//    }

    @ApiOperation("投注信息，每条比赛右边的小箭头点击接口")
    @PostMapping(value = "bet/info" )
    public BetResp betInfo(@RequestBody @Valid BetReq req){
        Schedules schedules = iSchedulesService.queryOne(req.getMatchId());
        BizAssert.notNull(schedules, BizErrCode.PARAM_ERROR_DESC,"matchId");
        Leagues league = iLeaguesService.lambdaQuery().eq(Leagues::getLeagueId,schedules.getLeagueId()).one();
        BizAssert.notNull(league, BizErrCode.PARAM_ERROR_DESC,"matchId");

        List<OrderInfo> list = _queryAlOrder(req);
        BetResp resp = new BetResp();
        resp.setLeague(BeanUtil.copy(league, LeagueResp.class));
        resp.setMatch(BeanUtil.copy(schedules, MatchResp.class));
        resp.setList(list.stream().map(o->{
            BetOddsResp betOddsResp = BeanUtil.copy(o,BetOddsResp.class);
            betOddsResp.setAmount(o.getBetRmbAmount());
            return betOddsResp;
        }).collect(Collectors.toList()));

        return resp;
    }

    private List<OrderInfo> _queryAlOrder(BetReq req){
        LambdaQueryChainWrapper<OrderInfo> wrapper =orderInfoService.lambdaQuery()
                .eq(OrderInfo::getMatchId,req.getMatchId())
                .ge(OrderInfo::getCreateTime, req.getTime())
                .eq(OrderInfo::getOddsType, req.getOddsType());

        if (req.getBetAmount() != null && req.getBetAmount().compareTo(BigDecimal.ZERO) > 0) {
            wrapper.ge(OrderInfo::getBetAmount, req.getBetAmount());
        }

        Integer userType = UserContext.getUserType();
        if (userType == UserTypeEnum.PROXY_ONE.v) {
            wrapper.eq(OrderInfo::getProxy1, UserContext.getUserNo());
        } else if (userType == UserTypeEnum.PROXY_TWO.v) {
            wrapper.eq(OrderInfo::getProxy2, UserContext.getUserNo());

        } else if (userType == UserTypeEnum.PROXY_THREE.v) {
            wrapper.eq(OrderInfo::getProxy3, UserContext.getUserNo());
        }

        if (StringUtil.isNotBlank(req.getUserIds())) {
            String[] uu = req.getUserIds().split(",");
            wrapper.in(OrderInfo::getUserId,Lists.newArrayList(uu));

        }

        return wrapper.list();
    }

}
