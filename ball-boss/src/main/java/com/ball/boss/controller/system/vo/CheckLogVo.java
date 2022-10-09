package com.ball.boss.controller.system.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

@Data
@ApiModel(description = "审核日志信息")
public class CheckLogVo {
    @ApiModelProperty("业务编号")
    private String bizId;

    @ApiModelProperty("审核类型 1初审 2复审")
    private Integer checkType;

    @ApiModelProperty("审核结果 1通过 2不通过 ")
    private Integer checkResult;

    @ApiModelProperty("审核人编号 ")
    private String checkUserId;

    @ApiModelProperty("审核人姓名")
    private String checkUserName;

    @ApiModelProperty("备注")
    private String remark;

    @ApiModelProperty("审核时间")
    private Date createTime;
}
