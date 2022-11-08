package com.ball.proxy.controller.order;

import com.ball.base.context.UserContext;
import com.ball.biz.bet.enums.OddsType;
import com.ball.biz.bet.enums.Sport;
import com.ball.biz.enums.UserTypeEnum;
import com.ball.biz.order.entity.OrderInfo;
import com.ball.biz.order.service.IOrderInfoService;
import com.ball.proxy.controller.order.vo.instant.*;
import com.google.common.collect.Lists;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
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

    @ApiOperation("总览")
    @PostMapping(value = "overview" )
    public OverviewResp overview(@RequestBody @Valid OverviewReq req){
        Integer userType = UserContext.getUserType();
        List<OrderInfo> all = null;
        if (userType == UserTypeEnum.PROXY_ONE.v) {
            all = orderInfoService.lambdaQuery().eq(OrderInfo::getProxy1, UserContext.getUserNo()).ge(OrderInfo::getCreateTime, req.getTime()).list();
        } else if (userType == UserTypeEnum.PROXY_TWO.v) {
            all = orderInfoService.lambdaQuery().eq(OrderInfo::getProxy2, UserContext.getUserNo()).ge(OrderInfo::getCreateTime, req.getTime()).list();

        } else if (userType == UserTypeEnum.PROXY_THREE.v) {
            all = orderInfoService.lambdaQuery().eq(OrderInfo::getProxy3, UserContext.getUserNo()).ge(OrderInfo::getCreateTime, req.getTime()).list();
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

                        inplayBetAmount = inplayBetAmount.add(_calTypeAmount(o,req.getType()));
                        break;
                    case NONE:
                    case EARLY_ODDS:
                        earlyBetCount++;
                        earlyBetAmount = earlyBetAmount.add(_calTypeAmount(o,req.getType()));
                        break;
                    case INSTANT_ODDS:
                        todayBetCount++;
                        todayBetAmount = todayBetAmount.add(_calTypeAmount(o,req.getType()));
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
            return order.getBetAmount();
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

    @ApiOperation("滚球")
    @PostMapping(value = "inplay" )
    public List<LeagueMatchResp> inplay(@RequestBody @Valid TodayReq req){
        return Lists.newArrayList();
    }

    @ApiOperation("今日")
    @PostMapping(value = "today" )
    public List<LeagueMatchResp> today(@RequestBody @Valid TodayReq req){
        return Lists.newArrayList();
    }

    @ApiOperation("早盘")
    @PostMapping(value = "early" )
    public List<LeagueMatchResp> early(@RequestBody @Valid EarlyReq req){
        return Lists.newArrayList();
    }

    @ApiOperation("已开赛")
    @PostMapping(value = "started" )
    public List<LeagueMatchResp> started(@RequestBody @Valid StartedReq req){
        return Lists.newArrayList();
    }

    @ApiOperation("投注信息，每条比赛右边的小箭头点击接口")
    @PostMapping(value = "bet/info" )
    public List<BetResp> betInfo(@RequestBody @Valid BetReq req){
        return Lists.newArrayList();
    }

}
