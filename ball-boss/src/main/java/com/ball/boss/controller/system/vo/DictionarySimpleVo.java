package com.ball.boss.controller.system.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@ApiModel(description = "字典信息")
public class DictionarySimpleVo {
    @ApiModelProperty("字典对照字段")
    @NotBlank(message = "对照字段不可为空")
    @Length(max = 80, message = "对照字段最大为80字符")
    private String field;

    @ApiModelProperty("对照字段名称")
    @NotBlank(message = "对照字段名称不可为空")
    @Length(max = 20, message = "对账字段说明最大为20字符")
    private String fieldName;

    @ApiModelProperty("对照码")
    @NotBlank(message = "对照码不可为空")
    @Length(max = 120, message = "对照码最大为120字符")
    private String code;

    @ApiModelProperty("代码描述")
    @NotBlank(message = "代码描述不可为空")
    @Length(max = 100, message = "代码描述最大为100")
    private String codeDesc;

    @ApiModelProperty("排序号(序号越小排序越靠前)")
    @NotNull(message = "排序号不可为空")
    private Integer sortNo;
}
