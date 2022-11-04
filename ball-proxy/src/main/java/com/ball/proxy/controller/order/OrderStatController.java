package com.ball.proxy.controller.order;

import com.ball.proxy.controller.order.vo.stat.*;
import com.ball.proxy.service.order.BizOrderStatService;
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
import java.util.Map;

/**
 * @author lhl
 * @date 2022/11/1 下午2:58
 */
@Api(tags = "订单统计相关")
@RestController
@RequestMapping("/proxy/order/stat")
public class OrderStatController {
    @Autowired
    private BizOrderStatService bizOrderStatService;

    @ApiOperation("赛事结果概要（key:YESTERDAY 昨天；TODAY 今天）")
    @PostMapping(value = "summary" )
    public Map<String, List<OrderSummaryResp>> summary(){
        return bizOrderStatService.summary();
    }

    @ApiOperation("注单报表 - 登2")
    @PostMapping(value = "proxy2/report" )
    public List<Proxy2ReportResp> proxy2Report(@RequestBody @Valid BaseReportReq req){
        return Lists.newArrayList();
    }

    @ApiOperation("注单报表 - 登3")
    @PostMapping(value = "proxy3/report" )
    public List<Proxy3ReportResp> proxy3Report(@RequestBody @Valid BaseReportReq req){
        return Lists.newArrayList();
    }

    @ApiOperation("注单报表 - 会员")
    @PostMapping(value = "user/report" )
    public List<UserReportResp> userReport(@RequestBody @Valid BaseReportReq req){
        return Lists.newArrayList();
    }

    @ApiOperation("主页 - 绩效概况")
    @PostMapping(value = "summary/report" )
    public List<SummaryReportResp> summary(@RequestBody @Valid SummaryReportReq req){
        return Lists.newArrayList();
    }

    @ApiOperation("主页 - 占成收入")
    @PostMapping(value = "income/report" )
    public List<IncomeReportResp> incomeReport(){
        return Lists.newArrayList();
    }

    @ApiOperation("主页 - 投注人数")
    @PostMapping(value = "betCount/report" )
    public List<BetCountReportResp> betCountReport(){
        return Lists.newArrayList();
    }

    @ApiOperation("主页 - 实货量")
    @PostMapping(value = "validAmount/report" )
    public List<ValidAmountReportResp> validAmountReport(){
        return Lists.newArrayList();
    }

    @ApiOperation("主页 - 输赢")
    @PostMapping(value = "win/report" )
    public List<WinReportResp> winReport(){
        return Lists.newArrayList();
    }
}
