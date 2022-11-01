package com.ball.biz.order.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.time.LocalDateTime;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import com.ball.base.model.Paging;

/**
 * <p>
 * 月帐期数表
 * </p>
 *
 * @author lhl
 * @since 2022-11-01
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class MonthlyAccountPeriod extends Paging {

    /**
     * 自增编号
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 年
     */
    private String periodYear;

    /**
     * 开始日期
     */
    private LocalDateTime startDate;

    /**
     * 结束日期
     */
    private LocalDateTime endDate;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;


}
