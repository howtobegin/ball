package com.ball.app.controller.account.vo;

import com.ball.base.model.Paging;
import com.ball.biz.account.enums.PlayTypeEnum;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

/**
 * @author ab
 * @date 2022/10/20 下午12:01
 */
@Getter
@Setter
public class UserConfigReq extends Paging {
    /**
     * 类型
     */
    @ApiModelProperty("类型 HOE 让球, 大 / 小, 单 / 双; HOE_INPAY 滚球让球, 滚球大 / 小, 滚球单 / 双;" +
            " WINNER_AND_WINNER_INPAY 独赢, 滚球独赢; OTHER 其他玩法; OTHER_INPAY 滚球其他")
    @NotBlank
    private String type;

    /**
     * 运动
     */
    @ApiModelProperty("运动 FOOTBALL 足球")
    @NotBlank
    private String sport;
}
