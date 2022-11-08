package com.ball.proxy.service.order;

import com.alibaba.fastjson.JSON;
import com.ball.base.context.UserContext;
import com.ball.base.model.Const;
import com.ball.base.util.BeanUtil;
import com.ball.biz.account.entity.SettlementPeriod;
import com.ball.biz.account.service.ISettlementPeriodService;
import com.ball.biz.enums.UserTypeEnum;
import com.ball.biz.order.entity.OrderStat;
import com.ball.biz.order.entity.OrderSummary;
import com.ball.biz.order.service.IOrderStatService;
import com.ball.biz.order.service.IOrderSummaryService;
import com.ball.proxy.controller.order.vo.stat.FourOneReportResp;
import com.ball.proxy.controller.order.vo.stat.OrderSummaryResp;
import com.ball.proxy.controller.order.vo.stat.SummaryReportReq;
import com.ball.proxy.controller.order.vo.stat.SummaryReportResp;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;
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
        log.info("dateType {} start {} end {}", req.getDateType(), start, end);
        SummaryReportResp resp = summaryByDate(start, end, req.getProxyUserId());
        // 如果是昨天，需要和前一天做比较
        if (req.getDateType() == 1) {
            start = start.plusDays(-1);
            end = start.plusDays(-1);
            SummaryReportResp beforeDay = summaryByDate(start, end, req.getProxyUserId());
            log.info("beforeDay {}", JSON.toJSONString(beforeDay));
            BigDecimal beforeProfitRate = Optional.ofNullable(resp.getProfitRate()).orElse(BigDecimal.ZERO);
            resp.setProfitRateCompareYesterday(beforeProfitRate.subtract(beforeDay.getProfitRate()));
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
        List<Long> proxy = null;
        // 如果是登1，proxyUserId代表登2
        Integer userType = UserContext.getUserType();
        if (UserTypeEnum.PROXY_ONE.isMe(userType)) {
            proxy = proxy(proxyUserId, null);
        }
        // 如果是登2，proxyUserId代表登3
        else if (UserTypeEnum.PROXY_TWO.isMe(userType)) {
            proxy = proxy(null, proxyUserId);
        } else {
            return SummaryReportResp.builder().build();
        }
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
        List<Long> proxy = proxy(null,null);
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

    public List<Long> proxy(Long proxy2Id, Long proxy3Id) {
        Long loginProxyUserId = UserContext.getUserNo();
        Integer userType = UserContext.getUserType();
        if (UserTypeEnum.PROXY_ONE.isMe(userType)) {
            return Lists.newArrayList(loginProxyUserId, proxy2Id, proxy3Id);
        } else if (UserTypeEnum.PROXY_TWO.isMe(userType)) {
            Lists.newArrayList(UserContext.getProxyUid(), loginProxyUserId, proxy3Id);
        } else if (UserTypeEnum.PROXY_THREE.isMe(userType)) {
            String[] split = UserContext.getProxyInfo().split(Const.RELATION_SPLIT);
            List<Long> proxy = Lists.newArrayList();
            proxy.addAll(Stream.of(split).map(Long::parseLong).collect(Collectors.toList()));
            proxy.add(loginProxyUserId);
            return proxy;
        }

        return null;
    }

    private String toPlainString(BigDecimal value, String defaultString) {
        return Optional.ofNullable(value).map(BigDecimal::stripTrailingZeros).map(BigDecimal::toPlainString).orElse(defaultString);
    }
}
