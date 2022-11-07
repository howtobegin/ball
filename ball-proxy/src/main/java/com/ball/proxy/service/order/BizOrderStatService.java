package com.ball.proxy.service.order;

import com.ball.base.context.UserContext;
import com.ball.base.model.Const;
import com.ball.base.util.BeanUtil;
import com.ball.biz.account.entity.SettlementPeriod;
import com.ball.biz.account.service.ICurrencyService;
import com.ball.biz.account.service.ISettlementPeriodService;
import com.ball.biz.enums.UserTypeEnum;
import com.ball.biz.order.entity.OrderStat;
import com.ball.biz.order.entity.OrderSummary;
import com.ball.biz.order.service.IOrderStatService;
import com.ball.biz.order.service.IOrderSummaryService;
import com.ball.biz.user.entity.UserInfo;
import com.ball.biz.user.service.IUserInfoService;
import com.ball.proxy.controller.order.vo.stat.*;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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
    @Autowired
    private ICurrencyService currencyService;
    @Autowired
    private ISettlementPeriodService settlementPeriodService;

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

    /**
     * where prox2=xxx and date > start and date < end group by p2,currency
     *
     * @param req
     * @return
     */
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

    public static void main(String[] args) {
        LocalDate today = LocalDate.now();
        LocalDate lastWeek = today.minusWeeks(1);

        LocalDate start = lastWeek.with(DayOfWeek.MONDAY);
        LocalDate end = lastWeek.with(DayOfWeek.SUNDAY);

        System.out.println(start);
        System.out.println(end);
    }

    /**
     * 主页 - 绩效概况
     */
    public SummaryReportResp summary(SummaryReportReq req) {
        LocalDate start = LocalDate.now();
        LocalDate end = start;
        // 昨天
        if (req.getDateType() == 1) {
            start = LocalDate.now().plusDays(-1);
            end = start;
        }
        // 上周
        else if (req.getDateType() == 2) {
            LocalDate lastWeek = LocalDate.now().plusWeeks(-1);
            start = lastWeek.with(DayOfWeek.MONDAY);
            end = lastWeek.with(DayOfWeek.SUNDAY);
        }
        // 本期
        else {
            SettlementPeriod sp = settlementPeriodService.currentPeriod();
            start = sp.getStartDate().toLocalDate();
            end = sp.getEndDate().toLocalDate();
        }
        SummaryReportResp resp = summaryByDate(start, end);
        // 如果是昨天，需要和前一天做比较
        if (req.getDateType() == 1) {
            start = start.plusDays(-1);
            end = start.plusDays(-1);
            SummaryReportResp beforeDay = summaryByDate(start, end);
            resp.setProfitRateCompareYesterday(resp.getProfitRate().subtract(beforeDay.getProfitRate()));
            resp.setWinAmountCompareYesterday(resp.getWinAmount().subtract(beforeDay.getWinAmount()));
            resp.setValidAmountCompareYesterday(resp.getValidAmount().subtract(beforeDay.getValidAmount()));
        }

        return resp;
    }

    private SummaryReportResp summaryByDate(LocalDate start, LocalDate end) {
        List<Long> proxy = proxy();
        Long proxyOne = proxy.get(0), proxyTwo = proxy.get(1), proxyThree = proxy.get(2);
        OrderStat stat = orderStatService.sumByDateAndProxy(start, end, proxyOne, proxyTwo, proxyThree);
        BigDecimal resultAmount = Optional.ofNullable(stat).map(OrderStat::getResultAmount).orElse(BigDecimal.ZERO).setScale(2, BigDecimal.ROUND_DOWN);
        BigDecimal validAmount = Optional.ofNullable(stat).map(OrderStat::getValidAmount).orElse(BigDecimal.ZERO).setScale(2, BigDecimal.ROUND_DOWN);
        BigDecimal profitRate = BigDecimal.ZERO.setScale(2);
        if (validAmount.compareTo(BigDecimal.ZERO) != 0) {
            profitRate = resultAmount.multiply(Const.HUNDRED).divide(validAmount, 2, BigDecimal.ROUND_DOWN);
        }
        return SummaryReportResp.builder()
                .profitRate(profitRate)
                .winAmount(resultAmount)
                .validAmount(validAmount)
                .build();
    }


    private List<Proxy2ReportResp> translateToProxy2Report(List<OrderStat> list) {
        List<Long> proxy3UserIds = list.stream().map(OrderStat::getProxy3).distinct().collect(Collectors.toList());
        List<UserInfo> proxy3Users = userInfoService.lambdaQuery().in(UserInfo::getId, proxy3UserIds).list();
        Map<Long, UserInfo> userIdToUser = proxy3Users.stream().collect(Collectors.toMap(UserInfo::getId, Function.identity()));

        List<Proxy2ReportResp> translateList =list.stream().map(stat -> translateToProxy2ReportOne(stat, userIdToUser)).collect(Collectors.toList());

        List<Proxy2ReportResp> ret = Lists.newArrayList();
        // 根据代理3，分组求和
        translateList.stream().collect(Collectors.groupingBy(Proxy2ReportResp::getProxyAccount))
                .entrySet().stream().map(e -> {
            List<Proxy2ReportResp> values = e.getValue();
            Proxy2ReportResp resp = new Proxy2ReportResp();

            resp.setProxyAccount(values.get(0).getProxyAccount());
            resp.setProxyName(values.get(0).getProxyName());

            Long betCount = 0L;
            BigDecimal betAmount = BigDecimal.ZERO;
            BigDecimal validAmount = BigDecimal.ZERO;
            BigDecimal userWinAmount = BigDecimal.ZERO;
            BigDecimal proxyCurrencyAmount = BigDecimal.ZERO;
            BigDecimal proxyResultAmount = BigDecimal.ZERO;
            BigDecimal proxyAmount = BigDecimal.ZERO;
            BigDecimal proxyResultAmount2 = BigDecimal.ZERO;
            BigDecimal proxyValidAmount = BigDecimal.ZERO;
            for (Proxy2ReportResp v : values) {
                betCount += v.getBetCount();
                betAmount = betAmount.add(v.getBetAmount());
                validAmount = validAmount.add(v.getValidAmount());
                userWinAmount = userWinAmount.add(v.getUserWinAmount());
                proxyCurrencyAmount = proxyCurrencyAmount.add(v.getProxyCurrencyAmount());
                proxyResultAmount = proxyResultAmount.add(v.getProxyResultAmount());
                proxyAmount = proxyAmount.add(v.getProxyAmount());
                proxyResultAmount2 = proxyResultAmount.add(v.getProxyResultAmount2());
                proxyValidAmount = proxyValidAmount.add(v.getProxyValidAmount());
            }
            resp.setBetCount(betCount);
            resp.setBetAmount(betAmount);
            resp.setValidAmount(validAmount);
            resp.setUserWinAmount(userWinAmount);
            resp.setProxyCurrencyAmount(proxyCurrencyAmount);
            resp.setProxyResultAmount(proxyResultAmount);
            resp.setProxyAmount(proxyAmount);
            resp.setProxyResultAmount2(proxyResultAmount2);
            resp.setProxyValidAmount(proxyValidAmount);

            return resp;
        }).collect(Collectors.toList());

        return ret;
    }

    private Proxy2ReportResp translateToProxy2ReportOne(OrderStat stat, Map<Long, UserInfo> userIdToUser) {
        Proxy2ReportResp resp = BeanUtil.copy(stat, Proxy2ReportResp.class);
        UserInfo user = userIdToUser.get(stat.getProxy2());
        if (user != null) {
            resp.setProxyAccount(user.getAccount());
            resp.setProxyName(user.getUserName());
        }
        resp.setBetCount(stat.getBetCount());
        String currency = stat.getBetCurrency();
        // 下注金额，RMB
        resp.setBetAmount(calcRmb(stat.getBetAmount(), currency));
        resp.setValidAmount(calcRmb(stat.getValidAmount(), currency));
        // 会员(RMB) = 输赢 + 退水
        BigDecimal userWinAmount = stat.getResultAmount().add(stat.getBackwaterAmount());
        resp.setUserWinAmount(calcRmb(userWinAmount, currency));
        // 代理商币值，原币种
        resp.setProxyCurrencyAmount(stat.getResultAmount());
        // 代理商，RMB
        resp.setProxyResultAmount(calcRmb(stat.getResultAmount(), currency));
        // 代理商占成
        resp.setProxyAmount(stat.getProxy2Amount());
        // 代理商结果 = 代理商
        resp.setProxyResultAmount2(resp.getProxyResultAmount());
        // 代理商实货量
        resp.setProxyValidAmount(resp.getValidAmount());
        // 总代理占成

        // 总代理结果
        return resp;
    }

    private BigDecimal calcRmb(BigDecimal amount, String currency) {
        BigDecimal rmbRate = currencyService.getRmbRate(currency);
        return amount.multiply(rmbRate);
    }

    private List<Long> proxy() {
        String proxyInfo = UserContext.getProxyInfo();
        if (StringUtils.isEmpty(proxyInfo)) {
            ArrayList<Long> proxy = Lists.newArrayList(UserContext.getUserNo());
            proxy.add(null);
            proxy.add(null);
            return proxy;
        }
        String[] split = proxyInfo.split(Const.RELATION_SPLIT);
        List<Long> proxy = Lists.newArrayList();
        proxy.addAll(Stream.of(split).map(Long::parseLong).collect(Collectors.toList()));
        proxy.add(UserContext.getUserNo());
        while (proxy.size() < 3) {
            proxy.add(null);
        }
        return proxy;
    }
}
