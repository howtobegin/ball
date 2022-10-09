package com.ball.boss.controller.system.vo.request;

import com.ball.base.model.Paging;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(description = "用户查询请求信息")
public class UserQueryRequestVo extends Paging {
    @ApiModelProperty("用户名(支持模糊查询)")
    private String userName;

    @ApiModelProperty("手机号")
    private String mobilePhone;
}
