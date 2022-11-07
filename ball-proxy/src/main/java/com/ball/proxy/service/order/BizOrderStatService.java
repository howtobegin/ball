package com.ball.proxy.service.order;

import com.ball.base.context.UserContext;
import com.ball.base.model.Const;
import com.ball.base.util.BeanUtil;
import com.ball.base.util.BizAssert;
import com.ball.biz.account.entity.SettlementPeriod;
import com.ball.biz.account.service.ICurrencyService;
import com.ball.biz.account.service.ISettlementPeriodService;
import com.ball.biz.enums.UserTypeEnum;
import com.ball.biz.exception.BizErrCode;
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


    private static final String KEY_FOUR_ONE_INCOME = "INCOME";
    private static final String KEY_FOUR_ONE_BET_COUNT = "BET_COUNT";
    private static final String KEY_FOUR_ONE_VALID_AMOUNT = "VALID_AMOUNT";
    private static final String KEY_FOUR_ONE_RESULT_AMOUNT = "RESULT_AMOUNT";


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
            if (sp == null) {
                return SummaryReportResp.builder().build();
            }
            start = sp.getStartDate().toLocalDate();
            end = sp.getEndDate().toLocalDate();
        }
        SummaryReportResp resp = summaryByDate(start, end, req.getProxyUserId());
        // 如果是昨天，需要和前一天做比较
        if (req.getDateType() == 1) {
            start = start.plusDays(-1);
            end = start.plusDays(-1);
            SummaryReportResp beforeDay = summaryByDate(start, end, req.getProxyUserId());
            resp.setProfitRateCompareYesterday(resp.getProfitRate().subtract(beforeDay.getProfitRate()));
            resp.setWinAmountCompareYesterday(resp.getWinAmount().subtract(beforeDay.getWinAmount()));
            resp.setValidAmountCompareYesterday(resp.getValidAmount().subtract(beforeDay.getValidAmount()));
        }
        // 如果是上周，需要和前一周做比较
        else if (req.getDateType() == 2) {
            start = start.plusWeeks(-1);
            end = end.plusWeeks(-1);
            SummaryReportResp beforeWeek = summaryByDate(start, end, req.getProxyUserId());
            resp.setProfitRateCompareYesterday(resp.getProfitRate().subtract(beforeWeek.getProfitRate()));
            resp.setWinAmountCompareYesterday(resp.getWinAmount().subtract(beforeWeek.getWinAmount()));
            resp.setValidAmountCompareYesterday(resp.getValidAmount().subtract(beforeWeek.getValidAmount()));
        }

        return resp;
    }

    private SummaryReportResp summaryByDate(LocalDate start, LocalDate end, Long proxyUserId) {
        List<Long> proxy = proxy(proxyUserId);
        Long proxyOne = proxy.get(0), proxyTwo = proxy.get(1), proxyThree = proxy.get(2);
        OrderStat stat = orderStatService.sumRmbByDateAndProxy(start, end, proxyOne, proxyTwo, proxyThree);
        BigDecimal resultAmount =Optional.ofNullable(stat).map(OrderStat::getResultAmount).orElse(BigDecimal.ZERO).setScale(2, BigDecimal.ROUND_DOWN);
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

    /**
     * 主页 - 四合一接口：占成收入(1)/投注人数(2)/实货量(3)/输赢(4)
     */
    public Map<String, List<FourOneReportResp>> fourReport() {
        // 代理
        List<Long> proxy = proxy(UserContext.getUserNo());
        // 本期
        SettlementPeriod sp = settlementPeriodService.currentPeriod();
        if (sp == null) {
            return Maps.newHashMap();
        }
        LocalDate start = sp.getStartDate().toLocalDate();
        LocalDate end = sp.getEndDate().toLocalDate();
        LocalDate yesterday = LocalDate.now().plusDays(-1);
        end = end.isBefore(yesterday) ? end : yesterday;

        List<OrderStat> list = orderStatService.sumRmbGroupByDate(start, end, proxy.get(0), proxy.get(1), proxy.get(2));
        log.info("list'size {}",list.size());
        Integer userType = UserContext.getUserType();
        List<FourOneReportResp> incomeList = Lists.newArrayList();
        List<FourOneReportResp> betCountList = Lists.newArrayList();
        List<FourOneReportResp> validAmountList = Lists.newArrayList();
        List<FourOneReportResp> winReportList = Lists.newArrayList();
        for (OrderStat stat : list) {
            incomeList.add(translate2IncomeReport(stat, userType));
            betCountList.add(translate2BetCountReport(stat));
            validAmountList.add(translate2ValidAmountReport(stat));
            winReportList.add(translate2WinReport(stat));
        }
        Map<String, List<FourOneReportResp>> map = Maps.newHashMap();
        map.put(KEY_FOUR_ONE_INCOME, fill(incomeList, start, end, "0.0"));
        map.put(KEY_FOUR_ONE_BET_COUNT, fill(betCountList, start, end, "0"));
        map.put(KEY_FOUR_ONE_VALID_AMOUNT, fill(validAmountList, start, end, "0.0"));
        map.put(KEY_FOUR_ONE_RESULT_AMOUNT, fill(winReportList, start, end, "0.0"));
        return map;
    }

    private List<FourOneReportResp> fill(List<FourOneReportResp> list, LocalDate start, LocalDate end, String defaultString) {
        List<FourOneReportResp> newList = Lists.newArrayList();
        int index = 0;
        for (; index < list.size();) {
            FourOneReportResp resp = list.get(index);
            if (resp.getDate().isEqual(start)) {
                newList.add(resp);

                index ++;
            } else {
                newList.add(FourOneReportResp.builder()
                        .date(start)
                        .amount(defaultString)
                        .build());
            }
            start = start.plusDays(1);
        }
        while (start.isBefore(end)) {
            newList.add(FourOneReportResp.builder().date(start).amount(defaultString).build());
            start = start.plusDays(1);
        }
        return newList;
    }

    private FourOneReportResp translate2IncomeReport(OrderStat stat, Integer userType) {
        FourOneReportResp resp = FourOneReportResp.builder()
                .date(stat.getBetDate())
                .build();
        String defaultString = "0.0";
        if (UserTypeEnum.PROXY_ONE.isMe(userType)) {
            resp.setAmount(toPlainString(stat.getProxy1RmbAmount(), defaultString));
        } else if (UserTypeEnum.PROXY_TWO.isMe(userType)) {
            resp.setAmount(toPlainString(stat.getProxy2RmbAmount(), defaultString));
        } else if (UserTypeEnum.PROXY_THREE.isMe(userType)) {
            resp.setAmount(toPlainString(stat.getProxy3RmbAmount(), defaultString));
        } else {
            resp.setAmount(toPlainString(BigDecimal.ZERO, defaultString));
        }
        return resp;
    }

    private FourOneReportResp translate2BetCountReport(OrderStat stat) {
        return FourOneReportResp.builder()
                .date(stat.getBetDate())
                .amount(stat.getBetCount().toString())
                .build();
    }

    private FourOneReportResp translate2ValidAmountReport(OrderStat stat) {
        return FourOneReportResp.builder()
                .date(stat.getBetDate())
                .amount(toPlainString(stat.getValidRmbAmount(), "0.0"))
                .build();
    }

    private FourOneReportResp translate2WinReport(OrderStat stat) {
        return FourOneReportResp.builder()
                .date(stat.getBetDate())
                .amount(toPlainString(stat.getResultRmbAmount(), "0.0"))
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

    private List<Long> proxy(Long proxyUserId) {
        Long loginProxyUserId = UserContext.getUserNo();
        String proxyInfo = UserContext.getProxyInfo();
        if (proxyUserId != null && !loginProxyUserId.equals(proxyUserId)) {
            UserInfo proxy = userInfoService.getByUid(proxyUserId);
            BizAssert.notNull(proxy, BizErrCode.USER_NOT_EXISTS);
            proxyInfo = proxy.getProxyInfo();
            loginProxyUserId = proxyUserId;
        }

        if (StringUtils.isEmpty(proxyInfo)) {
            ArrayList<Long> proxy = Lists.newArrayList(loginProxyUserId);
            proxy.add(null);
            proxy.add(null);
            return proxy;
        }
        String[] split = proxyInfo.split(Const.RELATION_SPLIT);
        List<Long> proxy = Lists.newArrayList();
        proxy.addAll(Stream.of(split).map(Long::parseLong).collect(Collectors.toList()));
        proxy.add(loginProxyUserId);
        while (proxy.size() < 3) {
            proxy.add(null);
        }
        return proxy;
    }

    private String toPlainString(BigDecimal value, String defaultString) {
        return Optional.ofNullable(value).map(BigDecimal::stripTrailingZeros).map(BigDecimal::toPlainString).orElse(defaultString);
    }
}
