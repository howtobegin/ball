package com.ball.proxy.service.order;

import com.ball.base.context.UserContext;
import com.ball.base.util.BeanUtil;
import com.ball.biz.enums.UserTypeEnum;
import com.ball.biz.order.entity.OrderStat;
import com.ball.biz.order.entity.OrderSummary;
import com.ball.biz.order.service.IOrderStatService;
import com.ball.biz.order.service.IOrderSummaryService;
import com.ball.biz.user.entity.UserInfo;
import com.ball.biz.user.service.IUserInfoService;
import com.ball.proxy.controller.order.vo.stat.BaseReportReq;
import com.ball.proxy.controller.order.vo.stat.OrderSummaryResp;
import com.ball.proxy.controller.order.vo.stat.Proxy2ReportResp;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author lhl
 * @date 2022/11/3 上午11:32
 */
@Slf4j
@Component
public class BizOrderStatService {
    @Autowired
    private IOrderSummaryService orderSummaryService;
    @Autowired
    private IOrderStatService orderStatService;
    @Autowired
    private IUserInfoService userInfoService;

    private static final String KEY_TODAY = "TODAY";
    private static final String KEY_YESTERDAY = "YESTERDAY";

    public Map<String, List<OrderSummaryResp>> summary() {
        LocalDate today = LocalDate.now();

        List<OrderSummary> todaySummery = orderSummaryService.queryByDate(today);
        List<OrderSummary> yesterDaySummery = orderSummaryService.queryByDate(today.plusDays(-1));
        Map<String, List<OrderSummaryResp>> map = Maps.newHashMap();

        map.put(KEY_TODAY, todaySummery.stream().map(o -> BeanUtil.copy(o, OrderSummaryResp.class)).collect(Collectors.toList()));
        map.put(KEY_YESTERDAY, yesterDaySummery.stream().map(o -> BeanUtil.copy(o, OrderSummaryResp.class)).collect(Collectors.toList()));

        return map;
    }

    public List<Proxy2ReportResp> proxy2Report(BaseReportReq req) {
        // 当前代理用户
        Long proxyTwo = UserContext.getUserNo();
        Integer userType = UserContext.getUserType();
        if (!UserTypeEnum.PROXY_TWO.isMe(userType)) {
            log.warn("userId {} userType {} is not PROXY_TWO", proxyTwo, userType);
            return Lists.newArrayList();
        }
        // 找到上级
        Long proxyOne = Optional.ofNullable(UserContext.getProxyInfo()).map(Long::valueOf).orElse(-1L);

        List<OrderStat> orderStats = orderStatService.queryByDate(req.getStart(), req.getEnd(), proxyOne, proxyTwo, null);
        return translateToProxy2Report(orderStats);
    }

    private List<Proxy2ReportResp> translateToProxy2Report(List<OrderStat> list) {
        List<Long> proxy3UserIds = list.stream().map(OrderStat::getProxy3).distinct().collect(Collectors.toList());
        List<UserInfo> proxy3Users = userInfoService.lambdaQuery().in(UserInfo::getId, proxy3UserIds).list();
        Map<Long, UserInfo> userIdToUser = proxy3Users.stream().collect(Collectors.toMap(UserInfo::getId, Function.identity()));

        List<Proxy2ReportResp> ret =list.stream().map(stat -> {
            Proxy2ReportResp resp = BeanUtil.copy(stat, Proxy2ReportResp.class);
            UserInfo user = userIdToUser.get(stat.getProxy3());
            if (user != null) {
                resp.setProxyAccount(user.getAccount());
                resp.setProxyName(user.getUserName());
            }
            resp.setBetCount(stat.getBetCount());
            // 下注金额，RMB
            resp.setBetAmount(stat.getBetAmount());
            resp.setValidAmount(stat.getValidAmount());
            // 会员(RMB) = 输赢 + 退水
            resp.setUserWinAmount(stat.getResultAmount().add(stat.getBackwaterAmount()));
            // 会员币值，投的币种

            // 代理商市值





            return resp;
        }).collect(Collectors.toList());
        return ret;
    }
}
