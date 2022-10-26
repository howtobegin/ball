package com.ball.biz.order.entity;

import com.ball.base.model.Paging;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * <p>
 * 订单历史表
 * </p>
 *
 * @author lhl
 * @since 2022-10-26
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class OrderHistory extends Paging {

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
     * 主队最后得分（来自schedule表）
     */
    private Integer homeLastScore;

    /**
     * 客队最后得分（来自schedule表）
     */
    private Integer awayLastScore;

    /**
     * 投注结果金额（包含本金）
     */
    private BigDecimal resultAmount;

    /**
     * 结果:UNSETTLED 未结算；LOSE 输；WIN 赢；LOSE_HALF 输一半；WIN_HALF 赢一半；DRAW 平
     */
    private String betResult;

    /**
     * 状态:INIT 初始化；CONFIRM；已结算 SETTLED； 确认；FINISH 完成；CANCEL/MATCH_CANCEL 取消
     */
    private String status;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;


}
