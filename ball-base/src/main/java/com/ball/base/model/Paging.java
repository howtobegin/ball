package com.ball.base.model;

import com.baomidou.mybatisplus.annotation.TableField;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

/**
 * @author littlehow
 */
@Setter
@Getter
public class Paging {
    @ApiModelProperty("页码 从1开始计数")
    @NotNull(message = "pageIndex must be not null")
    @Min(value = 1, message = "page index start with 1")
    @TableField(exist = false)
    private Integer pageIndex;

    @ApiModelProperty("页容 区间[1, 1000]")
    @NotNull(message = "pageSize must be not null")
    @Range(min = 1, max = 1000, message = "pageSize [1, 1000]")
    @TableField(exist = false)
    private Integer pageSize;
}
