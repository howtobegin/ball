package com.ball.boss.service.system;




import com.ball.base.model.PageResult;
import com.ball.boss.dao.entity.BossRole;
import com.ball.boss.service.system.model.RoleMenuInfo;

import java.util.List;

public interface RoleService {

    /**
     * 新增
     *
     * @param role
     * @return
     */
    String insert(BossRole role);

    /**
     * 修改
     *
     * @param role
     * @return
     */
    void update(BossRole role);

    /**
     * 删除角色
     *
     * @param roleId -- 角色编号
     * @return
     */
    void delete(String roleId);

    /**
     * 锁定或解锁信息
     *
     * @param roleId   -- 角色编号
     * @param lockFlag -- 锁定标志
     */
    void lockOrNot(String roleId, String lockFlag);

    /**
     * 查询所有角色
     *
     * @return
     */
    List<BossRole> queryAll();

    /**
     * 分页查询角色信息
     *
     * @param rolePo -- 条件
     * @return
     */
    PageResult<BossRole> queryPaging(BossRole rolePo);

    /**
     * 查询对应角色
     *
     * @param roleId -- 角色编号
     * @return
     */
    BossRole queryById(String roleId);

    /**
     * 查询角色已经对应的菜单信息
     *
     * @param roleId -- 角色编号
     * @return
     */
    RoleMenuInfo queryDetail(String roleId);
}
