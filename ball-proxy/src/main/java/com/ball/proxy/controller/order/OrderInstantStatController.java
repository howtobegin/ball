package com.ball.proxy.controller.order;

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
import java.util.List;

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
    public List<OverviewResp> overview(@RequestBody @Valid OverviewReq req){
        orderInfoService.lambdaQuery();
        OrderInfo orderInfo;
        return Lists.newArrayList();
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
