package com.ball.proxy.service.order;

import com.alibaba.fastjson.JSON;
import com.ball.base.context.UserContext;
import com.ball.base.util.BeanUtil;
import com.ball.base.util.BizAssert;
import com.ball.biz.account.service.ICurrencyService;
import com.ball.biz.enums.CurrencyEnum;
import com.ball.biz.enums.UserTypeEnum;
import com.ball.biz.exception.BizErrCode;
import com.ball.biz.match.entity.Schedules;
import com.ball.biz.match.service.ISchedulesService;
import com.ball.biz.order.entity.OrderInfo;
import com.ball.biz.order.entity.OrderStat;
import com.ball.biz.order.service.IOrderInfoService;
import com.ball.biz.order.service.IOrderStatService;
import com.ball.biz.user.bo.ProxyRateInfo;
import com.ball.biz.user.entity.UserExtend;
import com.ball.biz.user.entity.UserInfo;
import com.ball.biz.user.proxy.ProxyUserService;
import com.ball.biz.user.service.IUserExtendService;
import com.ball.biz.user.service.IUserInfoService;
import com.ball.proxy.controller.order.vo.stat.*;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 *
 * @author lhl
 * @date 2022/11/7 下午8:59
 */
@Slf4j
@Component
public class ProxyReportService {
    @Autowired
    private IOrderStatService orderStatService;
    @Autowired
    private IUserInfoService userInfoService;
    @Autowired
    private IUserExtendService userExtendService;
    @Autowired
    private BizOrderStatService bizOrderStatService;
    @Autowired
    private ProxyUserService proxyUserService;
    @Autowired
    private IOrderInfoService orderInfoService;
    @Autowired
    private ISchedulesService schedulesService;
    @Autowired
    private ICurrencyService currencyService;

    /**
     * 代理1和代理2显示一样的数据项
     * @param req
     * @return
     */
    public List<Proxy2ReportResp> proxyReportLevel1(BaseReportReq req) {
        int level = 1;
        if (UserTypeEnum.PROXY_ONE.isMe(UserContext.getUserType())) {
            level = req.getProxy1Id() == null ? level : 2;
        } else if (UserTypeEnum.PROXY_TWO.isMe(UserContext.getUserType())) {
            level = 2;
        }
        List<OrderStat> list = proxyReportData(req, level);
        return translateToProxy2Report(list, level);
    }

    /**
     * 第二级，显示某个登2下登3数据
     * @param req
     * @return
     */
    public List<Proxy3ReportResp> proxyReportLevel3(BaseReportReq req) {
        int level = 3;
        return translateToProxy3Report(proxyReportData(req, level));
    }

    /**
     * 第四级，显示某个登3下会员数据
     * @param req
     * @return
     */
    public List<Proxy3UserReportResp> proxyReportLevel4(BaseReportReq req) {
        // 当前代理用户
        List<Long> proxy = bizOrderStatService.proxy(req.getProxy2Id(), req.getProxy3Id());
        log.info("userId {} proxy {}", UserContext.getUserNo(), proxy);
        if (proxy == null) {
            return Lists.newArrayList();
        }
        Long proxyOne = proxy.get(0), proxyTwo = proxy.get(1), proxyThree = proxy.get(2);

        List<OrderStat> list = orderStatService.sumRmbGroupByUser(req.getStart(), req.getEnd(), proxyOne, proxyTwo, proxyThree);
        return translateToProxy3UserReport(list);
    }

    /**
     * 第五级，某个会员的详细投注
     * @param req
     * @return
     */
    public List<UserReportResp> userReportLevel5(BaseReportReq req) {
        log.info("req {}",JSON.toJSONString(req));
        BizAssert.notNull(req.getUserId(), BizErrCode.PARAM_ERROR_DESC,"userId");
        List<Long> proxy = bizOrderStatService.proxy(req.getProxy2Id(), req.getProxy3Id());
        log.info("userType {} proxy {}", UserContext.getUserType(), proxy);
        if (proxy == null) {
            return Lists.newArrayList();
        }

        List<OrderInfo> orders = orderInfoService.queryUserFinish(req.getStart(), req.getEnd(), proxy.get(0), proxy.get(1), proxy.get(2), req.getUserId(), req.isHasResult());
        if (orders.isEmpty()) {
            return Lists.newArrayList();
        }
        List<String> matchIds = orders.stream().map(OrderInfo::getMatchId).distinct().collect(Collectors.toList());
        Map<String, Schedules> matchIdToSchedules = schedulesService.batchQuery(matchIds).stream().collect(Collectors.toMap(Schedules::getMatchId, Function.identity()));
        return translateToUserReportResp(orders, matchIdToSchedules, req.isHasResult());
    }

    private List<OrderStat> proxyReportData(BaseReportReq req, int level) {
        // 当前代理用户
        Long userNo = UserContext.getUserNo();
        Integer userType = UserContext.getUserType();
        if (!UserTypeEnum.PROXY_ONE.isMe(userType) && !UserTypeEnum.PROXY_TWO.isMe(userType)) {
            log.warn("userId {} userType {} is not PROXY_ONE or TWO", userNo, userType);
            return Lists.newArrayList();
        }
        List<Long> proxy = bizOrderStatService.proxy(req.getProxy2Id(), req.getProxy3Id());
        log.info("proxy {}", proxy);
        if (proxy == null) {
            return Lists.newArrayList();
        }
        Long proxyOne = proxy.get(0), proxyTwo = proxy.get(1), proxyThree = proxy.get(2);

        return orderStatService.sumRmbGroupByProxy(req.getStart(), req.getEnd(), proxyOne, proxyTwo, proxyThree, level);
    }

    private Map<Long, UserInfo> proxyIdToUser(List<OrderStat> list, Integer level) {
        if (list.isEmpty()) {
            return Maps.newHashMap();
        }
        List<Long> ids = userIds(list, level);
        List<UserInfo> users = userInfoService.lambdaQuery().in(UserInfo::getId, ids).list();
        return users.stream().collect(Collectors.toMap(UserInfo::getId, Function.identity()));
    }

    private Map<Long, BigDecimal> proxyToRate(List<OrderStat> list, Integer level) {
        if (list.isEmpty()) {
            return Maps.newHashMap();
        }
        List<Long> ids = userIds(list, level);
        return userExtendService.getByUid(ids).stream().collect(Collectors.toMap(UserExtend::getId, UserExtend::getProxyRate));
    }

    private List<Long> userIds(List<OrderStat> list, Integer level) {
        return list.stream().map(stat -> {
            if (level == 1) {
                return stat.getProxy1();
            } else if (level == 2) {
                return stat.getProxy2();
            } else if (level == 3) {
                return stat.getProxy3();
            } else {
                return stat.getUserId();
            }
        }).distinct().collect(Collectors.toList());
    }

    private List<Proxy2ReportResp> translateToProxy2Report(List<OrderStat> list, Integer level) {
        if (list.isEmpty()) {
            return Lists.newArrayList();
        }
        Map<Long, UserInfo> proxy2IdToUser = proxyIdToUser(list, level);
        Map<Long, BigDecimal> proxy2Rate = proxyToRate(list, level);
        return list.stream().map(stat -> translateToProxy2ReportOne(stat, proxy2IdToUser, proxy2Rate, level)).collect(Collectors.toList());
    }

    private Proxy2ReportResp translateToProxy2ReportOne(OrderStat stat, Map<Long, UserInfo> userIdToUser, Map<Long, BigDecimal> proxy2Rate, Integer level) {
        Proxy2ReportResp resp = BeanUtil.copy(stat, Proxy2ReportResp.class);
        // 只有登1和登2用
        Long proxyId = level == 1 ? stat.getProxy1() : stat.getProxy2();
        UserInfo user = userIdToUser.get(proxyId);
        resp.setProxyId(proxyId);
        if (user != null) {
            resp.setProxyAccount(user.getAccount());
            resp.setProxyName(user.getUserName());
        }
        resp.setBetCount(stat.getBetCount());
        // 下注金额，RMB
        resp.setBetAmount(stat.getBetRmbAmount());
        resp.setValidAmount(stat.getValidRmbAmount());
        // 会员(RMB) = 输赢 + 退水
        BigDecimal resultRmbAmount = stat.getResultRmbAmount();
        BigDecimal userWinAmount = resultRmbAmount.add(stat.getBackwaterRmbAmount());
        resp.setUserWinAmount(userWinAmount);
        // 代理商 下线输赢和，RMB
        resp.setProxyResultAmount(resultRmbAmount);
        // 代理商结果 下线输赢和，RMB
        resp.setProxyResultAmount2(resultRmbAmount);
        // 总代理结果
        resp.setProxyResultAmount3(resultRmbAmount);
        // 总代理实货量
        resp.setProxyValidAmount(stat.getValidRmbAmount());
        // 总代理百分比
        resp.setProxyRate(proxy2Rate.get(proxyId));

        return resp;
    }

    private List<Proxy3ReportResp> translateToProxy3Report(List<OrderStat> list) {
        if (list.isEmpty()) {
            return Lists.newArrayList();
        }
        Map<Long, UserInfo> proxy3IdToUser = proxyIdToUser(list, UserTypeEnum.PROXY_THREE.v);
        Map<Long, BigDecimal> proxy3Rate = proxyToRate(list, UserTypeEnum.PROXY_THREE.v);

        List<Proxy3ReportResp> translateList = Lists.newArrayList();
        for (OrderStat stat : list) {
            UserInfo proxy = proxy3IdToUser.get(stat.getProxy3());
            translateList.add(Proxy3ReportResp.builder()
                    .proxyId(stat.getProxy3())
                    .proxyAccount(Optional.ofNullable(proxy).map(UserInfo::getAccount).orElse(null))
                    .proxyName(Optional.ofNullable(proxy).map(UserInfo::getUserName).orElse(null))
                    .betCount(stat.getBetCount())
                    .betAmount(stat.getBetRmbAmount())
                    .validAmount(stat.getValidRmbAmount())
                    // 输赢+退水
                    .userWinAmount(stat.getResultRmbAmount().add(stat.getBackwaterRmbAmount()))
                    // 不区分币种
                    .proxyCurrencyAmount(stat.getValidAmount())
                    .proxyResultAmount(stat.getResultRmbAmount())
                    .proxyAmount(stat.getProxy3RmbAmount())
                    .proxyResultAmount2(stat.getResultRmbAmount())
                    .proxyValidAmount(stat.getValidRmbAmount())
                    .proxy3Rate(proxy3Rate.get(stat.getProxy3()))
                    .proxyResultAmount3(stat.getResultRmbAmount())
                    .build());
        }


        return translateList;
    }

    private List<Proxy3UserReportResp> translateToProxy3UserReport(List<OrderStat> list) {
        if (list.isEmpty()) {
            return Lists.newArrayList();
        }
        List<Long> userIds = list.stream().map(OrderStat::getUserId).distinct().collect(Collectors.toList());
        List<UserInfo> users = userInfoService.lambdaQuery().in(UserInfo::getId, userIds).list();
        Map<Long, UserInfo> idToUser = users.stream().collect(Collectors.toMap(UserInfo::getId, Function.identity()));

        List<Proxy3UserReportResp> translateList = Lists.newArrayList();
        for (OrderStat stat : list) {
            ProxyRateInfo proxyRate = proxyUserService.getProxyRateByUid(stat.getUserId());

            UserInfo proxy = idToUser.get(stat.getUserId());
            translateList.add(Proxy3UserReportResp.builder()
                    .userId(stat.getUserId())
                    .userAccount(Optional.ofNullable(proxy.getAccount()).orElse(null))
                    .userName(Optional.ofNullable(proxy.getUserName()).orElse(null))
                    .betCount(stat.getBetCount())
                    .betAmount(stat.getBetRmbAmount())
                    .validAmount(stat.getValidRmbAmount())
                    // 输赢+退水
                    .userWinAmount(stat.getResultRmbAmount().add(stat.getBackwaterRmbAmount()))
                    // 不区分币种
                    .userCurrencyAmount(stat.getValidAmount())
                    .proxy3Percent(Optional.ofNullable(proxyRate).map(ProxyRateInfo::getProxyThreeRate).orElse(BigDecimal.ZERO))
                    .proxy3Amount(stat.getProxy3RmbAmount())
                    .build());
        }
        return translateList;
    }

    private List<UserReportResp> translateToUserReportResp(List<OrderInfo> orders, Map<String, Schedules> matchIdToSchedules, boolean hasResult) {
        if (orders.isEmpty()) {
            return Lists.newArrayList();
        }
        List<UserReportResp> ret = Lists.newArrayList();
        for (OrderInfo order : orders) {
            Schedules s = matchIdToSchedules.get(order.getMatchId());
            String currency = order.getBetCurrency();

            UserReportResp resp = UserReportResp.builder()
                    .userId(order.getUserId())
                    .betTime(order.getCreateTime())
                    .orderId(order.getOrderId())
                    .sport(order.getSport())
                    .leagueName(Optional.ofNullable(s).map(Schedules::getLeagueNameZh).orElse(null))
                    .homeName(Optional.ofNullable(s).map(Schedules::getHomeNameZh).orElse(null))
                    .awayName(Optional.ofNullable(s).map(Schedules::getAwayNameZh).orElse(null))
                    .betOption(order.getBetOption())
                    .handicapType(order.getHandicapType())
                    .instantHandicap(order.getInstantHandicap())
                    // 以下金额，都换成RMB
                    .betAmount(order.getBetRmbAmount())
                    .build();
            if (hasResult) {
                resp.setValidAmount(calcRmb(order.getValidAmount(), currency))
                        .setResultAmount(calcRmb(order.getResultAmount(), currency))
                        .setBetResult(order.getBetResult())
                        .setHomeScore(order.getHomeLastScore())
                        .setAwayScore(order.getAwayLastScore())
                        .setProxy3Amount(calcRmb(order.getProxy3Amount(), currency))
                        .setProxy2Amount(calcRmb(order.getProxy2Amount(), currency));
            }
            ret.add(resp);
        }
        return ret;
    }

    private BigDecimal calcRmb(BigDecimal amount, String currency) {
        if (amount == null) {
            return BigDecimal.ZERO;
        }
        if (CurrencyEnum.RMB.name().equalsIgnoreCase(currency)) {
            return amount.setScale(1, BigDecimal.ROUND_DOWN);
        }
        BigDecimal rmbRate = currencyService.getRmbRate(currency);
        return amount.multiply(rmbRate).setScale(1, BigDecimal.ROUND_DOWN);
    }

    /**
     *  * 登1，一条登1数据，无，Proxy2ReportResp
     *  *  点登1，登1下登2数据，proxy1Id，Proxy2ReportResp
     *  *  点登2，登2下登3数据，proxy2Id，Proxy3ReportResp
     *  *  点登3，登3下会员数据，proxy3Id，Proxy3UserReportResp
     *  *  点会员，会员下投注数据，userId，UserReportResp
     *  *
     *  * 登2，一条登2数据，无
     *  *  点登2，登2下登3数据，proxy2Id
     *  *  点登3，登3下会员数据，proxy3Id
     *  *  点会员，会员下投注数据，userId
     *  * 登3，一条登3数据，无
     *  *  点登3，登3下会员数据，proxy3Id
     *  *  点会员，会员下投注数据，userId
     * @param req
     * @return
     */
    private int anlyzeLevel(BaseReportReq req) {
        Integer userType = UserContext.getUserType();
        log.info("userType {}", userType);
        if (UserTypeEnum.PROXY_ONE.isMe(userType)) {
            int level = 1;
            if (req.getProxy1Id() != null) {
                level++;
            }
            if (req.getProxy2Id() != null) {
                level++;
            }
            if (req.getProxy3Id() != null) {
                level++;
            }
            if (req.getUserId() != null) {
                level++;
            }
            return level;
        } else if (UserTypeEnum.PROXY_TWO.isMe(userType)) {
            return req.getProxy1Id() == null ? 1 : 2;
        } else if (UserTypeEnum.PROXY_ONE.isMe(userType)) {
            return req.getProxy1Id() == null ? 1 : 2;
        }


        return 1;
    }
}
