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
 * 用户信息表
 * </p>
 *
 * @author littlehow
 * @since 2021-09-22
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class BossUserInfo extends Paging {

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
     * 用户名
     */
    private String userName;

    /**
     * 登陆帐户
     */
    private String account;

    /**
     * 密码
     */
    private String password;

    /**
     * 性别(0:未知;1:男;2:女)
     */
    private String sex;

    /**
     * 部门编号
     */
    private String deptId;

    /**
     * 锁定标志(1:锁定;0:激活)
     */
    private String locked;

    /**
     * 手机号区号
     */
    private String mobileArea;

    /**
     * 手机号
     */
    private String mobilePhone;

    /**
     * 谷歌验证私钥
     */
    private String googleKey;

    /**
     * 备注
     */
    private String remark;

    /**
     * 人员类型(1:业务员;2:管理员;3:系统内置人员)
     */
    private String userType;

    /**
     * 启用状态
     */
    private String enable;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;


}
