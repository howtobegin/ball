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
 * 
 * </p>
 *
 * @author atom
 * @since 2022-10-19
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class UserAccount extends Paging {

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 用户id
     */
    private Long userId;

    /**
     * 授信额度
     */
    private BigDecimal allowance;

    /**
     * 额度模式
     */
    private String allowanceMode;

    /**
     * 币种
     */
    private String currency;

    /**
     * 余额
     */
    private BigDecimal balance;

    /**
     * 用户类型，代理，会员
     */
    private String userType;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;

    /**
     * 冻结金额
     */
    private BigDecimal freezeAmount;


}
