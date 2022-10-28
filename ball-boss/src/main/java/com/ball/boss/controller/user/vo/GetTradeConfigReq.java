package com.ball.boss.controller.user.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * @author fanyongpeng
 * @date 10/28/22
 **/
@Data
@ApiModel(description = "请求")
public class GetTradeConfigReq {
    @ApiModelProperty("用户编号")
    @NotNull(message = "userNo 不能为空")
    private Long userNo;
}
