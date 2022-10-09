package com.ball.boss.controller.system.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

@Data
@ApiModel(description = "字典信息")
public class DictionaryBaseVo extends DictionarySimpleVo {
    @ApiModelProperty("编辑模式 0:只读;1:可编辑")
    @NotNull(message = "编辑模式不可为空")
    @Pattern(regexp = "[01]", message = "编辑模式只能为0或1")
    private String editMode;

    @ApiModelProperty("备注")
    @Length(max = 200, message = "备注最大为200字符")
    private String remark;
}
