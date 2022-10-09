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
 * 踢出日志表
 * </p>
 *
 * @author littlehow
 * @since 2021-09-22
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class BossKickOutLog extends Paging {

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
     * session编号
     */
    private String sessionId;

    /**
     * 登录ip地址
     */
    private String ip;

    /**
     * 终结ip(谁踢出的)
     */
    private String terminateIp;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;


}
