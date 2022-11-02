package com.ball.proxy.controller.account;

import com.ball.base.util.BeanUtil;
import com.ball.base.util.DateUtil;
import com.ball.biz.account.entity.SettlementPeriod;
import com.ball.biz.account.service.ICurrencyService;
import com.ball.biz.account.service.ISettlementPeriodService;
import com.ball.proxy.controller.account.vo.SettlementPeriodGetResp;
import com.ball.proxy.controller.account.vo.SettlementPeriodResp;
import com.ball.proxy.controller.currency.vo.CurrencyResp;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author fanyongpeng
 * @date 10/28/22
 **/

@Api(tags = "账期接口")
@RestController
@RequestMapping("/proxy/settlementperiod")
public class SettlementPeriodController {

    @Autowired
    ISettlementPeriodService iSettlementPeriodService;

    @ApiOperation("查询所有账期")
    @PostMapping(value = "/get" )
    public List<SettlementPeriodGetResp> get(){
        List<SettlementPeriod> list = iSettlementPeriodService.lambdaQuery().orderByDesc(SettlementPeriod::getId).list();
        List<SettlementPeriodGetResp>  res = (list.stream().collect(Collectors.groupingBy(s-> DateUtil.formatYear(s.getStartDate())))).entrySet().stream()
                .map(c->{
                    SettlementPeriodGetResp resp = new SettlementPeriodGetResp();
                    resp.setYear(c.getKey());
                    resp.setList(c.getValue().stream().map(v->{
                        SettlementPeriodResp period = BeanUtil.copy(v,SettlementPeriodResp.class);
                        period.setPeriod(DateUtil.formatDate(period.getStartDate())+" ~ " + DateUtil.formatDate(period.getEndDate()));
                        return period;
                    }).collect(Collectors.toList()));

                    return resp;
                }).collect(Collectors.toList());
        res.sort(new Comparator<SettlementPeriodGetResp>() {
            @Override
            public int compare(SettlementPeriodGetResp o1, SettlementPeriodGetResp o2) {
                return o1.getYear().compareTo(o2.getYear());
            }
        });
        return res;
    }



}
