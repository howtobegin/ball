package com.ball.boss.controller.system.vo.response;

import com.ball.boss.controller.system.vo.DictionaryBaseVo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

@Data
@ApiModel(description = "字典信息")
public class DictionaryResponseVo extends DictionaryBaseVo {
    @ApiModelProperty("字典编号")
    private String codeId;
    @ApiModelProperty("创建时间")
    private Date createTime;
    @ApiModelProperty("更新时间")
    private Date updateTime;
}
