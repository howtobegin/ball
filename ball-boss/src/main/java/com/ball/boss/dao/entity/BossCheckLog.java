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
 * 业务审核日志表
 * </p>
 *
 * @author JimChery
 * @since 2021-09-22
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class BossCheckLog extends Paging {

    /**
     * 自增编号
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 业务代码
     */
    private String bizCode;

    /**
     * 业务编号
     */
    private String bizId;

    /**
     * 审核类型 0初次提交 1初审 2复审
     */
    private Integer checkType;

    /**
     * 审核结果 1通过 2不通过
     */
    private Integer checkResult;

    /**
     * 审核人编号
     */
    private String checkUserId;

    /**
     * 审核人姓名
     */
    private String checkUserName;

    /**
     * 备注
     */
    private String remark;

    /**
     * 审核时间
     */
    private LocalDateTime createTime;


}
