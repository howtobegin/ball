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
 * 操作日志表
 * </p>
 *
 * @author littlehow
 * @since 2021-09-22
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class BossOperationLog extends Paging {

    /**
     * 自增编号
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 操作人编号
     */
    private String userId;

    /**
     * 操作人姓名
     */
    private String userName;

    /**
     * 操作业务
     */
    private String operationBiz;

    /**
     * 业务编号
     */
    private String bizId;

    /**
     * 操作类型 ADD/DELETE/UPDATE
     */
    private String operationType;

    /**
     * 操作时间
     */
    private LocalDateTime operationTime;

    /**
     * 操作说明
     */
    private String remark;


}
