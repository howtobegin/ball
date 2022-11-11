package com.ball.proxy.service;

import com.ball.base.context.UserContext;
import com.ball.base.util.BeanUtil;
import com.ball.biz.bet.enums.OrderStatus;
import com.ball.biz.enums.UserTypeEnum;
import com.ball.biz.match.entity.Schedules;
import com.ball.biz.match.service.ISchedulesService;
import com.ball.biz.order.entity.OrderInfo;
import com.ball.biz.order.service.IOrderInfoService;
import com.ball.proxy.controller.order.vo.OrderHistoryResp;
import com.ball.proxy.controller.order.vo.OrderResp;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author lhl
 * @date 2022/10/25 下午5:35
 */
@Slf4j
@Component
public class BizOrderQueryService {
    @Autowired
    private IOrderInfoService orderInfoService;
    @Autowired
    private ISchedulesService schedulesService;

    public List<OrderResp> current(Long userId) {
        Long proxyId = UserContext.getUserNo();
        Integer userType = UserContext.getUserType();

        List<String> statusList = OrderStatus.finishCodes(false);
        log.info("userNo {} statusList {}", proxyId, statusList);
        List<OrderInfo> records = orderInfoService.lambdaQuery()
                .eq(UserTypeEnum.PROXY_ONE.isMe(userType), OrderInfo::getProxy1, proxyId)
                .eq(UserTypeEnum.PROXY_TWO.isMe(userType), OrderInfo::getProxy2, proxyId)
                .eq(UserTypeEnum.PROXY_THREE.isMe(userType), OrderInfo::getProxy3, proxyId)
                .eq(OrderInfo::getUserId, userId)
                .in(OrderInfo::getStatus, statusList)
                .list();
        List<OrderResp> respList = records.stream().map(o->BeanUtil.copy(o, OrderResp.class)).collect(Collectors.toList());
        setOtherInfo(respList);
        return respList;
    }

    public List<OrderHistoryResp> history(Long userNo) {
        Long proxyId = UserContext.getUserNo();
        Integer userType = UserContext.getUserType();
        List<String> statusList = OrderStatus.finishCodes(true);
        log.info("proxyId {} userType {} userNo {} statusList {}", proxyId, userType, userNo, statusList);

        LocalDateTime end = LocalDateTime.now();
        LocalDateTime start = end.plusDays(-8);

        QueryWrapper<OrderInfo> query = new QueryWrapper<>();
        query.select("bet_date, sum(bet_amount) bet_amount, sum(result_amount) result_amount, sum(valid_amount) valid_amount, sum(backwater_amount) backwater_amount")
                .eq("user_id", userNo)
                .in("status", statusList)
                .ge("create_time", start)
                .lt("create_time", end)
                .groupBy("bet_date")
        ;
        List<OrderInfo> records = orderInfoService.lambdaQuery().getBaseMapper().selectList(query);
        List<OrderHistoryResp> respList = records.stream().map(o -> {
            OrderHistoryResp resp = BeanUtil.copy(o, OrderHistoryResp.class);
            if (o.getBetDate() != null) {
                resp.setDate(o.getBetDate());
                resp.setWeek(resp.getDate().getDayOfWeek().getValue());
            }
            // 这个是外层统计，就是输赢和+退水
            resp.setWinAmount(o.getResultAmount().add(o.getBackwaterAmount()));

            return resp;
        }).collect(Collectors.toList());
        return respList;
    }

    private List<OrderResp> setOtherInfo(List<OrderResp> list) {
        List<String> matchIds = list.stream().map(OrderResp::getMatchId).distinct().collect(Collectors.toList());
        if (matchIds.isEmpty()) {
            return list;
        }
        List<Schedules> schedules = schedulesService.batchQuery(matchIds);
        Map<String, Schedules> matchSchedule = schedules.stream().collect(Collectors.toMap(Schedules::getMatchId, Function.identity()));
        for (OrderResp resp : list) {
            Schedules s = matchSchedule.get(resp.getMatchId());
            if (s != null) {
                resp.setHomeName(s.getHomeNameZh());
                resp.setAwayName(s.getAwayNameZh());
                resp.setLeagueName(s.getLeagueNameZh());
                resp.setLeagueShortName(s.getLeagueShortName());
            }
            BigDecimal winAmount = calcWinAmount(OrderStatus.parse(resp.getStatus()), resp.getBetAmount(), resp.getBetOdds(), resp.getResultAmount(), resp.getBackwaterAmount());
            resp.setWinAmount(winAmount);
        }
        return list;
    }

    private BigDecimal calcWinAmount(OrderStatus status, BigDecimal betAmount, BigDecimal betOdds, BigDecimal resultAmount, BigDecimal backwaterAmount) {
        BigDecimal winAmount = BigDecimal.ZERO;
        if (status != null) {
            if (status == OrderStatus.FINISH) {
                winAmount = resultAmount.add(backwaterAmount);
            } else if (OrderStatus.isCancel(status)) {
            } else {
                winAmount = betAmount.multiply(betOdds);
            }
        }
        return winAmount;
    }
}
