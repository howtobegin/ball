package com.ball.boss.controller.system.vo.request;

import com.ball.boss.controller.system.vo.DictionaryBaseVo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
@ApiModel(description = "字典信息")
public class DictionaryUpdateVo extends DictionaryBaseVo {
    @ApiModelProperty("字典编号")
    @NotBlank(message = "字典编号不可为空")
    private String codeId;
}
