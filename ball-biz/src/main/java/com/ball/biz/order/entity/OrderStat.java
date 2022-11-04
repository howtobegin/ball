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
 * 订单代理商统计表
 * </p>
 *
 * @author lhl
 * @since 2022-11-01
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class OrderStat extends Paging {

    /**
     * 自增编号
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 投注日期
     */
    private LocalDate betDate;

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
     * 投注金额
     */
    private BigDecimal betAmount;

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
     * 投注人数
     */
    private Long betCount;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;


}
