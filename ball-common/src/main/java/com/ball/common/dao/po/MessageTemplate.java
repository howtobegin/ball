package com.ball.common.dao.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.time.LocalDateTime;

import com.ball.base.model.Paging;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;


/**
 * <p>
 * 短信邮件模板表
 * </p>
 *
 * @author JimChery
 * @since 2021-04-14
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class MessageTemplate extends Paging {

    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 邮件主题
     */
    private String subject;

    /**
     * 内容
     */
    private String content;

    /**
     * code码
     */
    private String templateCode;

    /**
     * 媒体类型 1:手机 2:邮件
     */
    private Integer media;

    /**
     * 语言 中文:zh-CN 英文:en-US
     */
    private String lang;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;


}
