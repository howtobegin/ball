package com.ball.proxy.controller.proxy;

import com.ball.base.context.UserContext;
import com.ball.base.util.BeanUtil;
import com.ball.base.util.BizAssert;
import com.ball.biz.account.entity.TradeConfig;
import com.ball.biz.account.service.ITradeConfigService;
import com.ball.biz.exception.BizErrCode;
import com.ball.biz.user.entity.UserInfo;
import com.ball.biz.user.service.IUserInfoService;
import com.ball.proxy.controller.proxy.vo.*;
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
@RequestMapping("/proxy/tradeconfig")
public class TradeConfigController {

    @Autowired
    ITradeConfigService tradeConfigService;
    @Autowired
    IUserInfoService iUserInfoService;

    @ApiOperation("查询代理退水限额配置")
    @PostMapping(value = "/getProxyConfig" )
    public List<AgentTradeConfigResp> getAgentConfig(@RequestBody @Valid GetTradeConfigReq req){
        return tradeConfigService.getUserConfig(req.getUserNo()).stream().map(a->{
          return BeanUtil.copy(a,AgentTradeConfigResp.class );
        }).collect(Collectors.toList());
    }

    @ApiOperation("修改代理退水限额配置")
    @PostMapping(value = "/updateProxyConfig" )
    public void updateProxyConfig(@RequestBody @Valid List<AgentTradeConfigReq> reqList) {
        reqList.forEach(r->{
            tradeConfigService.update(BeanUtil.copy(r, TradeConfig.class), UserContext.getUserNo());
        });
    }


    @ApiOperation("查询用退水限额配置")
    @PostMapping(value = "/getUserConfig" )
    public List<UserTradeConfigResp> getUserConfig(@RequestBody @Valid GetTradeConfigReq req){
        return tradeConfigService.getUserConfig(req.getUserNo()).stream().map(a->{
            UserTradeConfigResp resp = BeanUtil.copy(a,UserTradeConfigResp.class );
            resp.setV(tradeConfigService.getUserRate(a));
            return resp;
        }).collect(Collectors.toList());
    }

    @ApiOperation("修改用户退水限额配置")
    @PostMapping(value = "/updateUserConfig" )
    public void updateUserConfig(@RequestBody @Valid List<UserTradeConfigReq> reqList) {
        Long userNo = reqList.get(0).getUserNo();
        UserInfo userInfo = iUserInfoService.getByUid(userNo);
        BizAssert.notNull(userInfo, BizErrCode.USER_NOT_EXISTS);
        reqList.forEach(r->{
            TradeConfig config = BeanUtil.copy(r, TradeConfig.class);
            tradeConfigService.setUserRate(config, r.getV());
            tradeConfigService.update(config, userInfo.getProxyUserId());
        });
    }





}
