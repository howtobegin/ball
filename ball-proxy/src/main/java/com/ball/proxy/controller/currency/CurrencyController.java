package com.ball.proxy.controller.currency;

import com.ball.base.util.BeanUtil;
import com.ball.biz.account.service.ICurrencyService;
import com.ball.proxy.controller.currency.vo.CurrencyResp;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author fanyongpeng
 * @date 10/28/22
 **/

@Api(tags = "币种管理接口")
@RestController
@RequestMapping("/boss/currency")
public class CurrencyController {

    @Autowired
    ICurrencyService iCurrencyService;

    @ApiOperation("查询币种信息")
    @PostMapping(value = "/get" )
    public List<CurrencyResp> getAgentConfig(){
        return iCurrencyService.lambdaQuery().list().stream().map(a->{
          return BeanUtil.copy(a,CurrencyResp.class );
        }).collect(Collectors.toList());
    }

}
