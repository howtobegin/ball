package com.ball.boss.controller.user.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * @author fanyongpeng
 * @date 10/28/22
 **/

@Data
@ApiModel(description = "代理退水限额配置")
public class AgentTradeConfigResp {

    private Long id;

    /**
     * user id
     */
    @ApiModelProperty("用户id")
    private Long userNo;

    /**
     * 类型
     */
    @ApiModelProperty("类型 HOE 让球, 大 / 小, 单 / 双; HOE_INPAY 滚球让球, 滚球大 / 小, 滚球单 / 双;" +
            " WINNER_AND_WINNER_INPAY 独赢, 滚球独赢; OTHER 其他玩法; OTHER_INPAY 滚球其他")
    private String type;

    /**
     * A类用户退水比例
     */
    @ApiModelProperty("A类用户退水比例")
    private BigDecimal a;

    /**
     * b用户退水比例
     */
    @ApiModelProperty("b用户退水比例")
    private BigDecimal b;

    /**
     * c类用户退水比例
     */
    @ApiModelProperty("c类用户退水比例")
    private BigDecimal c;

    /**
     * d类用户退水比例
     */
    @ApiModelProperty("d类用户退水比例")
    private BigDecimal d;

    /**
     * 运动
     */
    @ApiModelProperty("运动 FOOTBALL 足球")
    private String sport;

    /**
     * 单场最高限额
     */
    @ApiModelProperty("单场最高限额")
    private BigDecimal matchLimit;

    /**
     * 单注最高限额
     */
    @ApiModelProperty("单注最高限额")
    private BigDecimal orderLimit;

    /**
     * 单注最低
     */
    @ApiModelProperty("单注最低")
    private BigDecimal min;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;

}
