package com.ball.biz.user.entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import com.ball.base.model.Paging;

/**
 * <p>
 * 用户扩展信息
 * </p>
 *
 * @author JimChery
 * @since 2022-10-26
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class UserExtend extends Paging {

    /**
     * 用户编号
     */
    private Long id;

    /**
     * 用户类型  1:会员 2:代理
     */
    private Integer userType;

    /**
     * 代理分成
     */
    private BigDecimal proxyRate;

    /**
     * 总代理分成
     */
    private BigDecimal totalProxyRate;

    /**
     * 盘口类型
     */
    private String handicapType;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;


}
