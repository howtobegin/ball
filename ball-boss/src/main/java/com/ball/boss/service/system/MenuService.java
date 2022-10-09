package com.ball.boss.service.system;





import com.ball.boss.dao.entity.BossMenu;

import java.util.List;

public interface MenuService {
    /**
     * 添加菜单
     *
     * @param menu -- 菜单信息
     */
    String insert(BossMenu menu);

    /**
     * 修改菜单
     *
     * @param menu -- 菜单信息
     */
    void update(BossMenu menu);

    /**
     * 删除菜单
     * 1.有子节点的菜单不能删除，必须先删除完子节点才能删除
     * 2.已经将菜单赋给角色的不能删除，必须从角色里将权限剔除才能删除
     *
     * @param menuId -- 菜单编号
     */
    void delete(String menuId);

    /**
     * 查询所有菜单信息
     *
     * @return
     */
    List<BossMenu> queryAll();
}
