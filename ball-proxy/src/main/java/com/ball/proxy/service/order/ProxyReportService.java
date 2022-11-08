package com.ball.proxy.service.order;

import com.ball.base.context.UserContext;
import com.ball.base.util.BeanUtil;
import com.ball.base.util.BizAssert;
import com.ball.biz.enums.UserTypeEnum;
import com.ball.biz.exception.BizErrCode;
import com.ball.biz.order.entity.OrderStat;
import com.ball.biz.order.service.IOrderStatService;
import com.ball.biz.user.bo.ProxyRateInfo;
import com.ball.biz.user.entity.UserExtend;
import com.ball.biz.user.entity.UserInfo;
import com.ball.biz.user.proxy.ProxyUserService;
import com.ball.biz.user.service.IUserExtendService;
import com.ball.biz.user.service.IUserInfoService;
import com.ball.proxy.controller.order.vo.stat.BaseReportReq;
import com.ball.proxy.controller.order.vo.stat.Proxy2ReportResp;
import com.ball.proxy.controller.order.vo.stat.Proxy3ReportResp;
import com.ball.proxy.controller.order.vo.stat.Proxy3UserReportResp;
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

    /**
     * 代理1和代理2显示一样的数据项
     * 1-1，都不传
     * 1-2，proxy2Id
     * 1-3，proxy2Id，proxy3Id
     * 第一级，显示登1或登2数据
     * @param req
     * @return
     */
    public List<Proxy2ReportResp> proxyReportLevel1(BaseReportReq req) {
        List<OrderStat> list = proxyReportData(req);
        return translateToProxy2Report(list, UserContext.getUserType());
    }

    /**
     * 第二级，显示某个登2下登3数据
     * @param req
     * @return
     */
    public List<Proxy3ReportResp> proxyReportLevel2(BaseReportReq req) {
        Integer userType = UserContext.getUserType();
        if (!UserTypeEnum.PROXY_TWO.isMe(userType)) {
            BizAssert.notNull(req.getProxy2Id(), BizErrCode.PARAM_ERROR_DESC, "proxy2Id");
        }
        return translateToProxy3Report(proxyReportData(req));
    }

    /**
     * 第三级，显示某个登3下会员数据
     * @param req
     * @return
     */
    public List<Proxy3UserReportResp> proxyReportLevel3(BaseReportReq req) {
        Integer userType = UserContext.getUserType();
        if (UserTypeEnum.PROXY_ONE.isMe(userType)) {
            BizAssert.notNull(req.getProxy2Id(), BizErrCode.PARAM_ERROR_DESC, "proxy2Id");
            BizAssert.notNull(req.getProxy3Id(), BizErrCode.PARAM_ERROR_DESC, "proxy3Id");
        }
        if (UserTypeEnum.PROXY_TWO.isMe(userType)) {
            BizAssert.notNull(req.getProxy3Id(), BizErrCode.PARAM_ERROR_DESC, "proxy3Id");
        }
        // 当前代理用户
        List<Long> proxy = bizOrderStatService.proxy(req.getProxy2Id(), req.getProxy3Id());
        Long proxyOne = proxy.get(0), proxyTwo = proxy.get(1), proxyThree = proxy.get(2);

        List<OrderStat> list = orderStatService.sumRmbGroupByUser(req.getStart(), req.getEnd(), proxyOne, proxyTwo, proxyThree);
        return translateToProxy3UserReport(list);
    }

    private List<OrderStat> proxyReportData(BaseReportReq req) {
        // 当前代理用户
        Long userNo = UserContext.getUserNo();
        Integer userType = UserContext.getUserType();
        if (!UserTypeEnum.PROXY_ONE.isMe(userType) && !UserTypeEnum.PROXY_TWO.isMe(userType)) {
            log.warn("userId {} userType {} is not PROXY_ONE or TWO", userNo, userType);
            return Lists.newArrayList();
        }
        List<Long> proxy = bizOrderStatService.proxy(req.getProxy2Id(), req.getProxy3Id());
        Long proxyOne = proxy.get(0), proxyTwo = proxy.get(1), proxyThree = proxy.get(2);

        return orderStatService.sumRmbGroupByProxy(req.getStart(), req.getEnd(), proxyOne, proxyTwo, proxyThree);
    }

    private Map<Long, UserInfo> proxyIdToUser(List<OrderStat> list, Integer userType) {
        if (list.isEmpty()) {
            return Maps.newHashMap();
        }
        List<Long> ids = userIds(list, userType);
        List<UserInfo> users = userInfoService.lambdaQuery().in(UserInfo::getId, ids).list();
        return users.stream().collect(Collectors.toMap(UserInfo::getId, Function.identity()));
    }

    private Map<Long, BigDecimal> proxyToRate(List<OrderStat> list, Integer userType) {
        if (list.isEmpty()) {
            return Maps.newHashMap();
        }
        List<Long> ids = userIds(list, userType);
        return userExtendService.getByUid(ids).stream().collect(Collectors.toMap(UserExtend::getId, UserExtend::getProxyRate));
    }

    private List<Long> userIds(List<OrderStat> list, Integer userType) {
        return list.stream().map(stat -> {
            if (UserTypeEnum.PROXY_ONE.isMe(userType)) {
                return stat.getProxy1();
            } else if (UserTypeEnum.PROXY_TWO.isMe(userType)) {
                return stat.getProxy2();
            } else if (UserTypeEnum.PROXY_THREE.isMe(userType)) {
                return stat.getProxy3();
            } else {
                return stat.getUserId();
            }
        }).distinct().collect(Collectors.toList());
    }

    private List<Proxy2ReportResp> translateToProxy2Report(List<OrderStat> list, Integer userType) {
        if (list.isEmpty()) {
            return Lists.newArrayList();
        }
        Map<Long, UserInfo> proxy2IdToUser = proxyIdToUser(list, userType);
        Map<Long, BigDecimal> proxy2Rate = proxyToRate(list, userType);
        return list.stream().map(stat -> translateToProxy2ReportOne(stat, proxy2IdToUser, proxy2Rate, userType)).collect(Collectors.toList());
    }

    private Proxy2ReportResp translateToProxy2ReportOne(OrderStat stat, Map<Long, UserInfo> userIdToUser, Map<Long, BigDecimal> proxy2Rate, Integer userType) {
        Proxy2ReportResp resp = BeanUtil.copy(stat, Proxy2ReportResp.class);
        // 只有登1和登2用
        Long proxyId = UserTypeEnum.PROXY_ONE.isMe(userType) ? stat.getProxy1() : stat.getProxy2();
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
}
