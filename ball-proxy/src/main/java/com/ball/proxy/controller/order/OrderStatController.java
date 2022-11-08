package com.ball.proxy.controller.order;

import com.ball.proxy.controller.order.vo.stat.*;
import com.ball.proxy.service.order.BizOrderStatService;
import com.ball.proxy.service.order.ProxyNoResultReportService;
import com.ball.proxy.service.order.ProxyReportService;
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
 * 注单报表
 * 登1，一条登1数据，无，Proxy2ReportResp
 *  点登1，登1下登2数据，proxy1Id，Proxy2ReportResp
 *  点登2，登2下登3数据，proxy2Id，Proxy3ReportResp
 *  点登3，登3下会员数据，proxy3Id，Proxy3UserReportResp
 *  点会员，会员下投注数据，userId，UserReportResp
 *
 * 登2，一条登2数据，无
 *  点登2，登2下登3数据，proxy2Id
 *  点登3，登3下会员数据，proxy3Id
 *  点会员，会员下投注数据，userId
 * 登3，一条登3数据，无
 *  点登3，登3下会员数据，proxy3Id
 *  点会员，会员下投注数据，userId
 *
 * @author lhl
 * @date 2022/11/1 下午2:58
 */
@Api(tags = "订单统计相关")
@RestController
@RequestMapping("/proxy/order/stat")
public class OrderStatController {
    @Autowired
    private BizOrderStatService bizOrderStatService;
    @Autowired
    private ProxyReportService proxyReportService;
    @Autowired
    private ProxyNoResultReportService proxyNoResultReportService;

    @ApiOperation("赛事结果概要（key:YESTERDAY 昨天；TODAY 今天）")
    @PostMapping(value = "summary" )
    public Map<String, List<OrderSummaryResp>> summary(){
        return bizOrderStatService.summary();
    }

    @ApiOperation("注单报表 - 登1或登2，第一级，第二级")
    @PostMapping(value = "proxy2/report" )
    public List<Proxy2ReportResp> proxy2Report(@RequestBody @Valid BaseReportReq req){
        return req.isHasResult() ? proxyReportService.proxyReportLevel1(req) : proxyNoResultReportService.proxyReportLevel1(req);
    }

    @ApiOperation("注单报表 - 第三级，内容是登3的统计信息")
    @PostMapping(value = "proxy3/report" )
    public List<Proxy3ReportResp> proxy3Report(@RequestBody @Valid BaseReportReq req){
        return req.isHasResult() ? proxyReportService.proxyReportLevel3(req) : proxyNoResultReportService.proxyReportLevel3(req);
    }

    @ApiOperation("注单报表 - 第四级，内容是登3的会员统计信息")
    @PostMapping(value = "proxy3/user/report" )
    public List<Proxy3UserReportResp> proxy3UserReport(@RequestBody @Valid BaseReportReq req){
        return req.isHasResult() ? proxyReportService.proxyReportLevel4(req) : proxyNoResultReportService.proxyReportLevel4(req);
    }

    @ApiOperation("注单报表 - 第五级，会员")
    @PostMapping(value = "user/report" )
    public List<UserReportResp> userReport(@RequestBody @Valid BaseReportReq req){
        return proxyReportService.userReportLevel5(req);
    }

    @ApiOperation("主页 - 绩效概况")
    @PostMapping(value = "summary/report" )
    public SummaryReportResp summary(@RequestBody @Valid SummaryReportReq req){
        return bizOrderStatService.summary(req);
    }

    @ApiOperation("主页 - 四合一接口：占成收入(INCOME)/投注人数(BET_COUNT)/实货量(VALID_AMOUNT)/输赢(RESULT_AMOUNT)")
    @PostMapping(value = "four/report" )
    public Map<String, List<FourOneReportResp>> fourReport(){
        return bizOrderStatService.fourReport();
    }
}
