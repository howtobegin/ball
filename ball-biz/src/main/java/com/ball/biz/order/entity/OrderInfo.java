package com.ball.biz.order.entity;

import com.ball.base.model.Paging;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * <p>
 * 订单表
 * </p>
 *
 * @author lhl
 * @since 2022-10-21
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class OrderInfo extends Paging {

    /**
     * 自增编号
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 订单编号
     */
    private String orderId;

    /**
     * 用户编号
     */
    private Long userId;

    /**
     * 用户名
     */
    private String userName;

    /**
     * 一级代理
     */
    private Long proxy1;

    /**
     * 二级代理
     */
    private Long proxy2;

    /**
     * 三级代理
     */
    private Long proxy3;

    /**
     * 运动类型：1 足球
     */
    private Integer sport;

    /**
     * 联赛ID
     */
    private String leagueId;

    /**
     * 比赛ID
     */
    private String matchId;

    /**
     * 1: Macauslot,3: Crown
     */
    private String companyId;

    /**
     * 盘口类型,让球，欧赔，大小，波胆，参考：HandicapType
     */
    private String handicapType;

    /**
     * 投注选项，选队伍的就是队伍ID，大小就是OVER或UNDER
     */
    private String betOption;

    /**
     * 投注赔率
     */
    private BigDecimal betOdds;

    /**
     * 投注类型：1 早盘；2 赛前即时；3 滚盘
     */
    private Integer oddsType;

    /**
     * 投注币种
     */
    private String betCurrency;

    /**
     * 投注金额
     */
    private BigDecimal betAmount;

    /**
     * 投注金额RMB
     */
    private BigDecimal betRmbAmount;

    /**
     * 投注结果:UNSETTLED 未结算；LOSE 输；WIN 赢；LOSE_HALF 输一半；WIN_HALF 赢一半；DRAW 平
     */
    private String betResult;

    /**
     * 投注结果金额（包含本金）
     */
    private BigDecimal resultAmount;

    /**
     * 有效金额
     */
    private BigDecimal validAmount;

    /**
     * 代理1收入或支出金额
     */
    private BigDecimal proxy1Amount;

    /**
     * 代理2收入或支出金额
     */
    private BigDecimal proxy2Amount;

    /**
     * 代理3收入或支出金额
     */
    private BigDecimal proxy3Amount;

    /**
     * 退水
     */
    private BigDecimal backwaterAmount;

    /**
     * 主队当前得分（来自schedule表）
     */
    private Integer homeCurrentScore;

    /**
     * 客队当前得分（来自schedule表）
     */
    private Integer awayCurrentScore;

    /**
     * 翻译后的即使赔率
     */
    private String instantHandicap;

    /**
     * 主队最后得分（来自schedule表）
     */
    private Integer homeLastScore;

    /**
     * 客队最后得分（来自schedule表）
     */
    private Integer awayLastScore;

    /**
     * 赔率数据
     */
    private String oddsData;

    private LocalDate betDate;

    /**
     * 比赛状态 0: Not started 1: First half 2: Half-time break 3: Second half 4: Extra time 5: Penalty -1: Finished -10: Cancelled -11: TBD -12: Terminated -13: Interrupted -14: Postponed
     */
    private Integer scheduleStatus;

    /**
     * 结算状态:0 未结算；1 已结算
     */
    private Integer settleStatus;

    /**
     * 状态:INIT 初始化；CONFIRM 确认；FINISH 完成；CANCEL/MATCH_CANCEL 取消
     */
    private String status;

    /**
     * 取消原因
     */
    private String reason;

    /**
     * 结束时间
     */
    private LocalDateTime finishTime;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;


}
