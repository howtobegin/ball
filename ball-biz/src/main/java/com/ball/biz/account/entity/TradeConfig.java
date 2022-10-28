package com.ball.biz.account.entity;

import java.math.BigDecimal;
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
 * 限额 退水配置
 * </p>
 *
 * @author atom
 * @since 2022-10-27
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class TradeConfig extends Paging {

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * user id
     */
    private Long userNo;

    /**
     * 类型
     */
    private String type;

    /**
     * A类用户退水比例
     */
    private Float a;

    /**
     * b用户退水比例
     */
    private Float b;

    /**
     * c类用户退水比例
     */
    private Float c;

    /**
     * d类用户退水比例
     */
    private Float d;

    /**
     * 运动
     */
    private String sport;

    /**
     * 单场最高限额
     */
    private BigDecimal matchLimit;

    /**
     * 单注最高限额
     */
    private BigDecimal orderLimit;

    /**
     * 单注最低
     */
    private BigDecimal min;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;

    /**
     * 用户级别 A B C D
     */
    private String userLevel;


}
