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
 * 菜单资源信息表
 * </p>
 *
 * @author JimChery
 * @since 2021-09-22
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class BossMenu extends Paging {

    /**
     * 自增编号
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 菜单编号
     */
    private String menuId;

    /**
     * 菜单名称
     */
    private String menuName;

    /**
     * 上级菜单编号
     */
    private String parentId;

    /**
     * 节点图标CSS类名
     */
    private String iconCls;

    /**
     * 展开状态(1:展开;0:收缩)
     */
    private String expanded;

    /**
     * 请求地址
     */
    private String request;

    /**
     * 菜单级别(0:树枝节点;1:叶子节点;2:按钮级别)
     */
    private String menuLevel;

    /**
     * 排序号
     */
    private Integer sortNo;

    /**
     * 备注
     */
    private String remark;

    /**
     * 节点图标
     */
    private String icon;

    /**
     * 菜单类型(1:系统菜单;0:业务菜单)
     */
    private String menuType;

    /**
     * 菜单来源(1:系统初始化;2:动态增加)
     */
    private String menuSource;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;


}
