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
 * 调账订单
 * </p>
 *
 * @author atom
 * @since 2022-10-25
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class BizAssetAdjustmentOrder extends Paging {

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 订单号
     */
    private String orderNo;

    private Long userNo;

    /**
     * 调账金额
     */
    private BigDecimal amount;

    private Integer status;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;

    /**
     * 来源方
     */
    private Long fromUserNo;

    /**
     * 来源方减少额度值
     */
    private BigDecimal fromUserAmount;

    /**
     * 用户币种
     */
    private String currency;

    /**
     * 来源方币种
     */
    private String fromUserCurrency;

    private BigDecimal oldBalance;

    private BigDecimal newBalance;


}
