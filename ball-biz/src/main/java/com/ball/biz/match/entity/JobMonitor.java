package com.ball.biz.match.entity;

import com.ball.base.model.Paging;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

/**
 * <p>
 * 
 * </p>
 *
 * @author lhl
 * @since 2022-11-07
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("ds_job_monitor")
public class JobMonitor extends Paging {

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    private String job;

    private String api;

    private LocalDateTime lastExecuteTime;

    private LocalDateTime lastReqTime;

    private LocalDateTime lastResTime;

    /**
     * 0: 失败; 1: 成功
     */
    private Boolean lastReqStatus;

    private String lastReqMessage;

    /**
     * 任务间隔时间(秒)
     */
    private Integer intervalValue;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 最后一次更新时间
     */
    private LocalDateTime updateTime;


}
