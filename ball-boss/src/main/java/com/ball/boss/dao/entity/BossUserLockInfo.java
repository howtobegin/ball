package com.ball.boss.dao.entity;

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
 * 锁定信息
 * </p>
 *
 * @author littlehow
 * @since 2021-09-22
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class BossUserLockInfo extends Paging {

    /**
     * 自增编号
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 用户编号
     */
    private String userId;

    /**
     * 锁定原因
     */
    private String remark;

    /**
     * 锁定类型 1:系统自动锁定 2:管理员锁定
     */
    private Integer lockType;

    /**
     * 锁定操作用户 系统自动:SYSTEM
     */
    private String operatorId;

    /**
     * 锁定操作用户名 系统自动:SYSTEM
     */
    private String operatorName;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;


}
