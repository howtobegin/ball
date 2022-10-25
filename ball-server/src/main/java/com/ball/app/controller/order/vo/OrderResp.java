package com.ball.app.controller.order.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

/**
 * @author lhl
 * @date 2022/10/25 上午11:01
 */
@Getter
@Setter
@ApiModel("订单请求信息")
public class OrderResp {
    /**
     * 订单编号
     */
    @ApiModelProperty("订单编号")
    private String orderId;

    /**
     * 用户编号
     */
    @ApiModelProperty("")
    private Long userId;

    /**
     * 用户名
     */
    @ApiModelProperty("")
    private String userName;

    /**
     * 比赛ID
     */
    @ApiModelProperty("")
    private String matchId;

    /**
     * 盘口类型,让球，欧赔，大小，波胆，参考：HandicapType
     */
    @ApiModelProperty("")
    private String handicapType;

    /**
     * 投注选项，选队伍的就是队伍ID，大小就是OVER或UNDER
     */
    @ApiModelProperty("投注选项，选队伍的就是队伍ID，大小就是OVER或UNDER")
    private String betOption;

    /**
     * 投注赔率
     */
    @ApiModelProperty("投注赔率")
    private BigDecimal betOdds;

    /**
     * 投注金额
     */
    @ApiModelProperty("投注金额")
    private BigDecimal betAmount;

    /**
     * 投注结果:UNSETTLED 未结算；LOSE 输；WIN 赢；LOSE_HALF 输一半；WIN_HALF 赢一半；DRAW 平
     */
    @ApiModelProperty("投注结果:UNSETTLED 未结算；LOSE 输；WIN 赢；LOSE_HALF 输一半；WIN_HALF 赢一半；DRAW 平")
    private String betResult;

    /**
     * 投注结果金额（包含本金）
     */
    @ApiModelProperty("投注结果金额")
    private BigDecimal resultAmount;

    /**
     * 赢金额（不包含）
     */
    @ApiModelProperty("赢金额")
    private BigDecimal winAmount;

    /**
     * 输金额
     */
    @ApiModelProperty("输金额")
    private BigDecimal loseAmount;

    /**
     * 主队当前得分（来自schedule表）
     */
    @ApiModelProperty("主队当前得分")
    private Integer homeCurrentScore;

    /**
     * 客队当前得分（来自schedule表）
     */
    @ApiModelProperty("客队当前得分")
    private Integer awayCurrentScore;

    /**
     * 翻译后的即使赔率
     */
    @ApiModelProperty("翻译后的即使赔率")
    private String instantHandicap;

    /**
     * 主队最后得分（来自schedule表）
     */
    @ApiModelProperty("主队最后得分")
    private Integer homeLastScore;

    /**
     * 客队最后得分（来自schedule表）
     */
    @ApiModelProperty("客队最后得分")
    private Integer awayLastScore;

    /**
     * 赔率数据
     */
    @ApiModelProperty("赔率数据")
    private String oddsData;

    /**
     * 比赛状态 0: Not started 1: First half 2: Half-time break 3: Second half 4: Extra time 5: Penalty -1: Finished -10: Cancelled -11: TBD -12: Terminated -13: Interrupted -14: Postponed
     */
    @ApiModelProperty("比赛状态")
    private Integer scheduleStatus;

    /**
     * 结算状态:0 未结算；1 已结算
     */
    @ApiModelProperty("结算状态:0 未结算；1 已结算")
    private Integer settleStatus;

    /**
     * 状态:INIT 初始化；CONFIRM 确认；FINISH 完成；CANCEL/MATCH_CANCEL 取消
     */
    @ApiModelProperty("状态:INIT 初始化；CONFIRM 确认；SETTLED 结算；FINISH 完成；CANCEL/MATCH_CANCEL 取消")
    private String status;
}