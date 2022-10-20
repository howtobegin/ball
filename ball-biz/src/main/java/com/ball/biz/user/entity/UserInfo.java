package com.ball.biz.user.entity;

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
 * @since 2022-10-19
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class UserInfo extends Paging {

    /**
     * 用户编号
     */
    private Long id;

    /**
     * 账号
     */
    private String account;

    /**
     * 登入账号
     */
    private String loginAccount;

    /**
     * 名称
     */
    private String userName;

    /**
     * 邮箱
     */
    private String email;

    /**
     * 手机区号
     */
    private String mobileArea;

    /**
     * 手机号
     */
    private String mobileNo;

    /**
     * 密码
     */
    private String password;

    /**
     * 修改密码标志
     */
    private Integer changePasswordFlag;

    /**
     * 锁屏密码
     */
    private String lockPassword;

    /**
     * 安全密码
     */
    private String secretPassword;

    /**
     * 用户类型  1:会员 2:代理
     */
    private Integer userType;

    /**
     * 代理账号
     */
    private String proxyAccount;

    /**
     * 代理用户编号
     */
    private Long proxyUserId;

    /**
     * 代理信息
     */
    private String proxyInfo;

    /**
     * 用户状态 1:正常 0:锁定
     */
    private Integer status;

    /**
     * 最后登录时间
     */
    private Long lastLogin;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;


}
