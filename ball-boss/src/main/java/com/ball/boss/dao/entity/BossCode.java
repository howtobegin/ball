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
 * 代码对照表
 * </p>
 *
 * @author JimChery
 * @since 2021-09-22
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class BossCode extends Paging {

    /**
     * 自增编号
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 对照ID
     */
    private String codeId;

    /**
     * 对照字段
     */
    private String field;

    /**
     * 对照字段名称
     */
    private String fieldName;

    /**
     * 代码
     */
    private String code;

    /**
     * 代码描述
     */
    private String codeDesc;

    /**
     * 启用状态(0:禁用;1:启用)
     */
    private String enabled;

    /**
     * 编辑模式(0:只读;1:可编辑)
     */
    private String editMode;

    /**
     * 排序号
     */
    private Integer sortNo;

    /**
     * 备注
     */
    private String remark;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;


}
