package com.ball.biz.user.entity;

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
 * 用户登录日志
 * </p>
 *
 * @author littlehow
 * @since 2022-10-19
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class UserLoginLog extends Paging {

    /**
     * 自增编号
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 用户编号
     */
    private Long userId;

    /**
     * session编号
     */
    private String sessionId;

    /**
     * 登录ip地址
     */
    private String ip;

    /**
     * 状态 1:正常 0:被踢出
     */
    private Integer status;

    /**
     * 终结session编号
     */
    private String terminateSid;

    /**
     * 终结ip(谁踢出的)
     */
    private String terminateIp;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;

}
