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
 * 出入金日志表
 * </p>
 *
 * @author atom
 * @since 2022-10-19
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class AssetChangeLog extends Paging {

    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 用户编号
     */
    private Long userId;

    /**
     * 币种
     */
    private String currency;

    /**
     * 业务编号
     */
    private String orderNo;

    /**
     * 资金类型
     */
    private String transactionType;

    /**
     * 发生额
     */
    private BigDecimal amount;

    /**
     * 手续费
     */
    private BigDecimal fee;

    /**
     * 方向 in:入金 out:出金
     */
    private String direct;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 操作前余额
     */
    private BigDecimal oldBalance;

    /**
     * 操作后余额
     */
    private BigDecimal newBalance;


}
