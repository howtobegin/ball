package com.ball.boss.service.system;




import com.ball.base.model.PageResult;
import com.ball.boss.dao.entity.BossRole;
import com.ball.boss.dao.entity.BossUserInfo;
import com.ball.boss.service.system.model.RoleMenuInfo;

import java.util.List;

/**
 * 用户模块
 */
public interface UserService {

    /**
     * 获取用户角色权限信息
     *
     * @param userId -- 用户编号
     * @return
     */
    RoleMenuInfo getUserRoleMenuInfo(String userId);

    /**
     * 获取用户对应的角色信息
     *
     * @param userId -- 用户编号
     * @return
     */
    List<BossRole> getUserRoles(String userId);

    /**
     * 新增用户
     *
     * @param userInfo -- 用户信息
     * @return -- 用户编号
     */
    String insert(BossUserInfo userInfo);

    /**
     * 修改用户
     *
     * @param userInfo -- 用户信息
     */
    void update(BossUserInfo userInfo);

    /**
     * 删除用户
     *
     * @param userId -- 用户编号
     */
    void delete(String userId);

    /**
     * 修改用户密码
     *
     * @param oldPassword -- 旧密码
     * @param newPassword -- 新密码
     */
    void changePassword(String oldPassword, String newPassword);

    /**
     * 修改用户锁定状态
     *
     * @param userId -- 用户编号
     * @param locked -- 锁定标志
     */
    void changeLock(String userId, String locked);

    /**
     * 查询用户信息
     *
     * @param userId -- 用户编号
     * @return -- 对应的用户信息
     */
    BossUserInfo queryById(String userId);

    /**
     * 根据用户和密码查询
     * @param accountId
     * @param password
     * @return
     */
    BossUserInfo queryByAccountAndPassword(String accountId, String password);

    /**
     * 分页查询用户信息
     *
     * @param userInfo -- 查询信息
     * @return -- 用户的分页信息
     */
    PageResult<BossUserInfo> queryPaging(BossUserInfo userInfo);

    /**
     * 检测用户权限
     * <p>
     * 判断指定用户是否有访问某个接口的权限，用于权限控制。
     * <p>
     * 注意：仅针对权限列表中的接口有用，对直接开放的公共接口无效
     *
     * @return 是否有权（true:有权；false：无权）
     */
    boolean checkPermission(String userId, String uri);

    void bindGoogle(String account, String code);

    void unbindGoogle(String account, String code);
}
