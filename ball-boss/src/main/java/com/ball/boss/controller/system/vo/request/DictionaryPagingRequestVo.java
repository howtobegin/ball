package com.ball.boss.controller.system.vo.request;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@Data
@ApiModel(description = "分页查询字典请求信息")
public class DictionaryPagingRequestVo {

    @ApiModelProperty("对照字段")
    private String field;

    @ApiModelProperty("页码(从1开始)")
    @NotNull(message = "页码不可为空")
    @Min(value = 1, message = "页码最小为1")
    private Integer pageIndex;

    @ApiModelProperty("页容[1-200]")
    @NotNull(message = "页容不可为空")
    @Range(min = 1, max = 200, message = "页容大小区间为[1-200]")
    private Integer pageSize;
}
