package com.ball.proxy.controller.account.vo;

import com.ball.base.model.Paging;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

/**
 * @author ab
 * @date 2022/10/20 下午12:01
 */
@Getter
@Setter
public class AccountGetReq {
    @ApiModelProperty("用户id")
    @NotNull
    private Long userNo;
}
