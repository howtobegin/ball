package com.ball.boss.controller.user.vo;

import io.swagger.annotations.ApiModel;
import lombok.Data;

import java.util.List;

/**
 * @author fanyongpeng
 * @date 10/28/22
 **/

@Data
@ApiModel(description = "代理退水限额配置")
public class AgentTradeConfigResp {


    List<AgentTradeConfigResp> getAgent(){
        return null;
    }
}
