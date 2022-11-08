package com.ball.proxy.service.order;

import com.alibaba.fastjson.JSON;
import com.ball.base.context.UserContext;
import com.ball.biz.bet.enums.OrderStatus;
import com.ball.biz.order.entity.OrderInfo;
import com.ball.biz.order.service.IOrderInfoService;
import com.ball.biz.user.entity.UserInfo;
import com.ball.biz.user.service.IUserInfoService;
import com.ball.proxy.controller.order.vo.stat.*;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 未有结果的
 * @author lhl
 * @date 2022/11/8 上午11:23
 */
@Slf4j
@Component
public class ProxyNoResultReportService {
    @Autowired
    private IOrderInfoService orderInfoService;
    @Autowired
    private BizOrderStatService bizOrderStatService;
    @Autowired
    private IUserInfoService userInfoService;

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
        List<Long> proxy = bizOrderStatService.proxy(req.getProxy2Id(), req.getProxy3Id());
        List<OrderInfo> orders = sum(req, proxy);
        return translateToProxy2ReportResp(orders, proxy);
    }

    /**
     * 第二级，显示某个登2下登3数据
     * @param req
     * @return
     */
    public List<Proxy3ReportResp> proxyReportLevel2(BaseReportReq req) {
        List<Long> proxy = bizOrderStatService.proxy(req.getProxy2Id(), req.getProxy3Id());
        List<OrderInfo> orders = sum(req, proxy);
        return translateToProxy3ReportResp(orders);
    }

    /**
     * 第三级，显示某个登3下会员数据
     * @param req
     * @return
     */
    public List<Proxy3UserReportResp> proxyReportLevel3(BaseReportReq req) {
        List<Long> proxy = bizOrderStatService.proxy(req.getProxy2Id(), req.getProxy3Id());
        List<OrderInfo> orders = sum(req, proxy);
        return translateToProxy3UserReportResp(orders);
    }

    private List<OrderInfo> sum(BaseReportReq req, List<Long> proxy) {
        Integer userType = UserContext.getUserType();
        log.info("req {} userId {} userType {} proxy {}", JSON.toJSONString(req), UserContext.getUserNo(), userType, proxy);
        if (proxy == null) {
            return Lists.newArrayList();
        }
        Long proxy1 = proxy.get(0), proxy2 = proxy.get(1), proxy3 = proxy.get(2);

        String groupBy = "proxy1";
        if (proxy2 != null) {
            groupBy += ",proxy2";
        }
        if (proxy3 != null) {
            groupBy += ",proxy3";
        }

        QueryWrapper<OrderInfo> query = new QueryWrapper<>();
        // 用id接收投注数量
        query.select(groupBy + ", sum(1) id, sum(bet_rmb_amount) bet_rmb_amount")
                .eq(proxy1 != null, "proxy1", proxy1)
                .eq(proxy2 != null, "proxy2", proxy2)
                .eq(proxy3 != null, "proxy3", proxy3)
                .ge("bet_date", req.getStart())
                .le("bet_date", req.getEnd())
                .eq("sport", req.getSport())
                .in("status", OrderStatus.finishCodes(false))
                .groupBy(groupBy);
        return orderInfoService.getBaseMapper().selectList(query);
    }

    public List<Proxy2ReportResp> translateToProxy2ReportResp(List<OrderInfo> list, List<Long> proxy) {
        List<Long> userIds = Lists.newArrayList();
        if (proxy.get(1) != null) {
            userIds = list.stream().map(OrderInfo::getProxy2).distinct().collect(Collectors.toList());
        } else if (proxy.get(0) != null) {
            userIds = list.stream().map(OrderInfo::getProxy1).distinct().collect(Collectors.toList());
        }
        List<UserInfo> users = userInfoService.lambdaQuery().in(UserInfo::getId, userIds).list();
        Map<Long, UserInfo> userIdToUser = users.stream().collect(Collectors.toMap(UserInfo::getId, Function.identity()));
        return list.stream().map(o -> {
            UserInfo p = null;
            if (proxy.get(1) != null) {
                p = userIdToUser.get(o.getProxy2());
            } else if (proxy.get(0) != null) {
                p = userIdToUser.get(o.getProxy1());
            }
            return Proxy2ReportResp.builder()
                    .proxyAccount(Optional.ofNullable(p).map(UserInfo::getAccount).orElse(null))
                    .proxyName(Optional.ofNullable(p).map(UserInfo::getUserName).orElse(null))
                    .betCount(o.getId())
                    .betAmount(o.getBetRmbAmount())
                    .build();

        }).collect(Collectors.toList());
    }

    public List<Proxy3ReportResp> translateToProxy3ReportResp(List<OrderInfo> list) {
        List<Long> userIds = list.stream().map(OrderInfo::getProxy3).distinct().collect(Collectors.toList());
        List<UserInfo> users = userInfoService.lambdaQuery().in(UserInfo::getId, userIds).list();
        Map<Long, UserInfo> userIdToUser = users.stream().collect(Collectors.toMap(UserInfo::getId, Function.identity()));
        return list.stream().map(o -> {
            UserInfo p = userIdToUser.get(o.getProxy3());
            return Proxy3ReportResp.builder()
                    .proxyAccount(Optional.ofNullable(p).map(UserInfo::getAccount).orElse(null))
                    .proxyName(Optional.ofNullable(p).map(UserInfo::getUserName).orElse(null))
                    .betCount(o.getId())
                    .betAmount(o.getBetRmbAmount())
                    .build();

        }).collect(Collectors.toList());
    }

    public List<Proxy3UserReportResp> translateToProxy3UserReportResp(List<OrderInfo> list) {
        List<Long> userIds = list.stream().map(OrderInfo::getUserId).distinct().collect(Collectors.toList());
        log.info("userIds size {}", userIds.size());
        List<UserInfo> users = Lists.newArrayList();
        if (!CollectionUtils.isEmpty(userIds)) {
            users = userInfoService.lambdaQuery().in(UserInfo::getId, userIds).list();
        }
        Map<Long, UserInfo> userIdToUser = users.stream().collect(Collectors.toMap(UserInfo::getId, Function.identity()));
        return list.stream().map(o -> {
            UserInfo p = userIdToUser.get(o.getProxy3());
            return Proxy3UserReportResp.builder()
                    .userAccount(Optional.ofNullable(p).map(UserInfo::getAccount).orElse(null))
                    .userName(Optional.ofNullable(p).map(UserInfo::getUserName).orElse(null))
                    .betCount(o.getId())
                    .betAmount(o.getBetRmbAmount())
                    .build();

        }).collect(Collectors.toList());
    }
}
