package com.ball.proxy.service.order;

import com.alibaba.fastjson.JSON;
import com.ball.base.context.UserContext;
import com.ball.biz.bet.enums.OrderStatus;
import com.ball.biz.enums.UserTypeEnum;
import com.ball.biz.order.entity.OrderInfo;
import com.ball.biz.order.service.IOrderInfoService;
import com.ball.biz.user.entity.UserInfo;
import com.ball.biz.user.service.IUserInfoService;
import com.ball.proxy.controller.order.vo.stat.BaseReportReq;
import com.ball.proxy.controller.order.vo.stat.Proxy2ReportResp;
import com.ball.proxy.controller.order.vo.stat.Proxy3ReportResp;
import com.ball.proxy.controller.order.vo.stat.Proxy3UserReportResp;
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
     * @param req
     * @return
     */
    public List<Proxy2ReportResp> proxyReportLevel1(BaseReportReq req) {
        List<Long> proxy = bizOrderStatService.proxy(req.getProxy2Id(), req.getProxy3Id());
        int level = 1;
        if (UserTypeEnum.PROXY_ONE.isMe(UserContext.getUserType())) {
            level = req.getProxy1Id() == null ? level : 2;
        } else if (UserTypeEnum.PROXY_TWO.isMe(UserContext.getUserType())) {
            level = 2;
        }
        List<OrderInfo> orders = sum(req, proxy, level);
        return translateToProxy2ReportResp(orders, proxy);
    }

    /**
     * 显示某个登2下登3数据
     * @param req
     * @return
     */
    public List<Proxy3ReportResp> proxyReportLevel3(BaseReportReq req) {
        List<Long> proxy = bizOrderStatService.proxy(req.getProxy2Id(), req.getProxy3Id());
        List<OrderInfo> orders = sum(req, proxy, 3);
        return translateToProxy3ReportResp(orders);
    }

    /**
     * 显示某个登3下会员数据
     * @param req
     * @return
     */
    public List<Proxy3UserReportResp> proxyReportLevel4(BaseReportReq req) {
        List<Long> proxy = bizOrderStatService.proxy(req.getProxy2Id(), req.getProxy3Id());
        List<OrderInfo> orders = sum(req, proxy, 4);
        return translateToProxy3UserReportResp(orders);
    }

    private List<OrderInfo> sum(BaseReportReq req, List<Long> proxy, int level) {
        Integer userType = UserContext.getUserType();
        log.info("req {} userId {} userType {} proxy {}", JSON.toJSONString(req), UserContext.getUserNo(), userType, proxy);
        if (proxy == null) {
            return Lists.newArrayList();
        }
        Long proxy1 = proxy.get(0), proxy2 = proxy.get(1), proxy3 = proxy.get(2);

        String groupBy = "proxy1";
        if (level == 2) {
            groupBy = "proxy1,proxy2";
        } else if (level == 3) {
            groupBy = "proxy1,proxy2,proxy3";
        } else if (level == 4) {
            groupBy = "proxy1,proxy2,proxy3,user_id";
        }

        QueryWrapper<OrderInfo> query = new QueryWrapper<>();
        // 用id接收投注数量
        query.select(groupBy + ", sum(1) id, sum(bet_rmb_amount) bet_rmb_amount")
                .eq(proxy1 != null, "proxy1", proxy1)
                .eq(proxy2 != null, "proxy2", proxy2)
                .eq(proxy3 != null, "proxy3", proxy3)
                .eq(req.getUserId() != null, "user_id", req.getUserId())
                .ge("bet_date", req.getStart())
                .le("bet_date", req.getEnd())
                .eq("sport", req.getSport())
                .in("status", OrderStatus.finishCodes(false))
                .groupBy(groupBy);
        return orderInfoService.getBaseMapper().selectList(query);
    }

    public List<Proxy2ReportResp> translateToProxy2ReportResp(List<OrderInfo> list, List<Long> proxy) {
        log.info("start list size {}",list.size());
        if (CollectionUtils.isEmpty(list)) {
            return Lists.newArrayList();
        }
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
        log.info("start list size {}",list.size());
        if (CollectionUtils.isEmpty(list)) {
            return Lists.newArrayList();
        }
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
        log.info("start list size {}",list.size());
        if (CollectionUtils.isEmpty(list)) {
            return Lists.newArrayList();
        }
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
                    .userId(o.getUserId())
                    .userAccount(Optional.ofNullable(p).map(UserInfo::getAccount).orElse(null))
                    .userName(Optional.ofNullable(p).map(UserInfo::getUserName).orElse(null))
                    .betCount(o.getId())
                    .betAmount(o.getBetRmbAmount())
                    .build();

        }).collect(Collectors.toList());
    }
}
