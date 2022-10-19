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
 * 币种
 * </p>
 *
 * @author atom
 * @since 2022-10-19
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class Currency extends Paging {

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    private String currencyCode;

    private String description;

    /**
     * 对人民币对汇率 1 currency_code = rate RMB
     */
    private BigDecimal rate;

    private Integer status;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;


}
