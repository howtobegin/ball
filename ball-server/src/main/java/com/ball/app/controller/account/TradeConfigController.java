package com.ball.app.controller.account;

import com.ball.app.controller.account.vo.UserTradeConfigResp;
import com.ball.base.context.UserContext;
import com.ball.base.util.BeanUtil;
import com.ball.biz.account.service.ITradeConfigService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author fanyongpeng
 * @date 10/28/22
 **/

@Api(tags = "退水限额配置管理接口")
@RestController
@RequestMapping("/boss/tradeconfig")
public class TradeConfigController {

    @Autowired
    ITradeConfigService tradeConfigService;


    @ApiOperation("查询代理退水限额配置")
    @PostMapping(value = "/getUserConfig" )
    public List<UserTradeConfigResp> getUserConfig(){
        return tradeConfigService.getUserConfig(UserContext.getUserNo()).stream().map(a->{
            UserTradeConfigResp resp = BeanUtil.copy(a,UserTradeConfigResp.class );
            resp.setV(tradeConfigService.getUserRate(a));
            return resp;
        }).collect(Collectors.toList());
    }

}
