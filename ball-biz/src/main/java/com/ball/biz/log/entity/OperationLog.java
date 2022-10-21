package com.ball.biz.log.entity;

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
 * 操作日志表
 * </p>
 *
 * @author atom
 * @since 2022-10-21
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class OperationLog extends Paging {

    /**
     * 自增编号
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 操作用户编号
     */
    private Long operationUid;

    /**
     * 操作账户
     */
    private String operationAccount;

    /**
     * 业务类型
     */
    private String bizType;

    /**
     * 子类型
     */
    private String bizChild;

    /**
     * 业务编号
     */
    private String bizId;

    /**
     * 操作说明
     */
    private String remark;

    /**
     * 操作时间
     */
    private LocalDateTime operationTime;


}
