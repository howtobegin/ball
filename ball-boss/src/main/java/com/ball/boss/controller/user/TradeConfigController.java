package com.ball.boss.controller.user;

import com.ball.base.util.BeanUtil;
import com.ball.biz.account.service.ITradeConfigService;
import com.ball.boss.controller.user.vo.AgentTradeConfigResp;
import com.ball.boss.controller.user.vo.GetTradeConfigReq;
import com.ball.boss.controller.user.vo.UserTradeConfigResp;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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
    List<AgentTradeConfigResp> getAgentConfig(@RequestBody @Valid GetTradeConfigReq req){
        return tradeConfigService.getUserConfig(req.getUserNo()).stream().map(a->{
          return BeanUtil.copy(a,AgentTradeConfigResp.class );
        }).collect(Collectors.toList());
    }


    @ApiOperation("查询代理退水限额配置")
    @PostMapping(value = "/getUserConfig" )
    List<UserTradeConfigResp> getUserConfig(@RequestBody @Valid GetTradeConfigReq req){
        return tradeConfigService.getUserConfig(req.getUserNo()).stream().map(a->{
            return BeanUtil.copy(a,UserTradeConfigResp.class );
        }).collect(Collectors.toList());
    }

}
