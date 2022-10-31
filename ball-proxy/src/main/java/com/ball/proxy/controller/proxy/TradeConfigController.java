package com.ball.proxy.controller.proxy;

import com.ball.base.util.BeanUtil;
import com.ball.biz.account.service.ITradeConfigService;
import com.ball.proxy.controller.proxy.vo.AgentTradeConfigResp;
import com.ball.proxy.controller.proxy.vo.GetTradeConfigReq;
import com.ball.proxy.controller.proxy.vo.UserTradeConfigResp;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
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
    @PostMapping(value = "/getAgentConfig" )
    public List<AgentTradeConfigResp> getAgentConfig(@RequestBody @Valid GetTradeConfigReq req){
        return tradeConfigService.getUserConfig(req.getUserNo()).stream().map(a->{
          return BeanUtil.copy(a,AgentTradeConfigResp.class );
        }).collect(Collectors.toList());
    }


    @ApiOperation("查询代理退水限额配置")
    @PostMapping(value = "/getUserConfig" )
    public List<UserTradeConfigResp> getUserConfig(@RequestBody @Valid GetTradeConfigReq req){
        return tradeConfigService.getUserConfig(req.getUserNo()).stream().map(a->{
            UserTradeConfigResp resp = BeanUtil.copy(a,UserTradeConfigResp.class );
            resp.setV(tradeConfigService.getUserRate(a));
            return resp;
        }).collect(Collectors.toList());
    }

}
