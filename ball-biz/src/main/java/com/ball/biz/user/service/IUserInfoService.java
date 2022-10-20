package com.ball.biz.user.service;

import com.ball.biz.user.entity.UserInfo;
import com.baomidou.mybatisplus.extension.service.IService;

import com.ball.common.service.IBaseService;

import java.util.List;

/**
 * <p>
 * 用户信息表 服务类
 * </p>
 *
 * @author littlehow
 * @since 2022-10-19
 */
public interface IUserInfoService extends IService<UserInfo>, IBaseService {
    String SPRING_SESSION_KEY_PREFIX = "ball:app:token:sessions:";
    String SPRING_SESSION_EXPIRE_PREFIX = SPRING_SESSION_KEY_PREFIX + "expires:";
    /**
     * 添加用户
     * @param account        - 账号
     * @param userName       - 用户名
     * @param password       - 密码
     * @param proxyAccount   - 代理账号
     * @return - 用户编号
     */
    Long addUser(String account, String userName, String password, String proxyAccount, Long proxyUid);

    /**
     * 根据用户编号获取用户信息
     * @param userId   - 用户编号
     * @return -
     */
    UserInfo getByUid(Long userId);

    /**
     * 根据代理信息获取对应的代理用户
     * @param proxyInfo -
     * @return
     */
    List<UserInfo> getByProxyInfo(String proxyInfo);

    /**
     * 根据账号获取用户编号
     * @param account    - 账号
     * @return -
     */
    UserInfo getByAccount(String account);

    /**
     * 根据登录账号获取用户编号
     * @param loginAccount   - 登录账号
     * @return -
     */
    UserInfo getByLogin(String loginAccount);

    /**
     * 修改密码
     * @param userId       - 用户编号
     * @param oldPassword  - 旧密码
     * @param newPassword  - 新密码
     */
    void changePassword(Long userId, String oldPassword, String newPassword);

    /**
     * 第一次修改密码
     * @param password - 密码
     */
    void firstChangePassword(Long userId, String password);

    /**
     * 修改登录账号
     * @param userId        - 用户编号
     * @param loginAccount  - 登录账号
     */
    void changeLogin(Long userId, String loginAccount);

    /**
     * 设置锁屏密码
     * @param userId         - 用户编号
     * @param lockPassword   - 锁屏密码
     */
    void setLockPassword(Long userId, String lockPassword);

    /**
     * 清除锁屏密码
     * @param userId  - 用户编号
     */
    void clearLockPassword(Long userId);

    /**
     * 登录
     * @param sessionId        - session编号
     * @param loginAccount     - 登入账户
     * @param password         - 密码
     * @return -
     */
    UserInfo login(String sessionId, String loginAccount, String password);

    /**
     * 登出
     * @param userId - 用户编号
     */
    void logout(Long userId);

}
