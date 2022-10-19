package com.ball.biz.base.entity;

import java.time.LocalDateTime;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import com.ball.base.model.Paging;

/**
 * <p>
 * 编号生成表
 * </p>
 *
 * @author littlehow
 * @since 2022-10-19
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class IdGen extends Paging {

    /**
     * 表名
     */
    private String tableName;

    /**
     * 序号
     */
    private Long seqNo;

    /**
     * 步进
     */
    private Integer incr;

    /**
     * 版本
     */
    private Long version;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;


}
