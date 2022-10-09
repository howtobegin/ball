package com.ball.boss.service.system;

import java.util.List;

public interface AuthorizeService {
    /**
     * 修改用户角色信息, 如果集合为空，则表示删除用户的所有角色
     *
     * @param roleIds -- 用户角色授权信息
     * @param userId  -- 用户编号
     */
    void updateUserAuthorize(List<String> roleIds, String userId);

    /**
     * 修改角色权限信息，如果集合为空，则表示删除角色的所有菜单
     *
     * @param menuIds -- 角色权限信息
     * @param roleId  -- 角色编号
     */
    void updateRoleAuthorize(List<String> menuIds, String roleId);
}
