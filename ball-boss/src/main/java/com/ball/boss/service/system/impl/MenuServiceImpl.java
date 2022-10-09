package com.ball.boss.service.system.impl;


import com.ball.base.util.BizAssert;
import com.ball.boss.dao.entity.BossMenu;
import com.ball.boss.dao.entity.BossRoleAuthorize;
import com.ball.boss.exception.BossErrorCode;
import com.ball.boss.service.IBossMenuService;
import com.ball.boss.service.IBossRoleAuthorizeService;
import com.ball.boss.service.system.BaseService;
import com.ball.boss.service.system.MenuService;
import com.ball.boss.service.system.model.MenuSource;
import com.ball.boss.service.system.model.OperationType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


@Slf4j
@Service
public class MenuServiceImpl extends BaseService implements MenuService {
    private static final String OPERATION_BIZ = "menu";

    @Autowired
    private IBossMenuService menuService;

    @Autowired
    private IBossRoleAuthorizeService roleAuthorizeService;

    @Override
    public String insert(BossMenu menu) {
        menu.setMenuId(getBizId());
        //用户添加
        menu.setMenuSource(MenuSource.USER_ADD.v);
        log.info("MenuService insert menu={}", toJsonString(menu));
        transactionSupport.execute(() -> {
            menuService.save(menu);
            logAssist.operationLog(OperationType.ADD, OPERATION_BIZ, menu.getMenuId());
        });

        return menu.getMenuId();
    }

    @Override
    public void update(BossMenu menu) {
        log.info("MenuService update role={}", toJsonString(menu));
        //先查询
        BossMenu dbMenu = menuService.lambdaQuery().eq(BossMenu::getMenuId, menu.getMenuId()).one();
        BizAssert.notNull(dbMenu, BossErrorCode.DATA_NOT_EXISTS);
        BizAssert.isTrue(MenuSource.isUserAdd(dbMenu.getMenuSource()), BossErrorCode.DATA_NOT_EDITABLE);
        menu.setId(dbMenu.getId());
        transactionSupport.execute(() -> {
            boolean flag = menuService.updateById(menu);
            BizAssert.isTrue(flag, BossErrorCode.UPDATE_FAIL);
            //记录日志
            logAssist.operationLog(OperationType.UPDATE, OPERATION_BIZ, menu.getMenuId());
        });

    }

    @Override
    public void delete(String menuId) {
        log.info("MenuService delete menuId={}", menuId);
        BossMenu dbMenu = menuService.lambdaQuery().eq(BossMenu::getMenuId, menuId).one();
        BizAssert.notNull(dbMenu, BossErrorCode.DATA_NOT_EXISTS);
        BizAssert.isTrue(MenuSource.isUserAdd(dbMenu.getMenuSource()), BossErrorCode.DATA_NOT_EDITABLE);
        //判断是否有下级菜单
        Integer count = menuService.lambdaQuery().eq(BossMenu::getParentId, menuId).count();
        BizAssert.isZero(count, BossErrorCode.HAS_CHILD);
        //判断菜单是否分配给角色
        count = roleAuthorizeService.lambdaQuery().eq(BossRoleAuthorize::getMenuId, menuId).count();
        BizAssert.isZero(count, BossErrorCode.DATA_NOT_EXISTS);
        transactionSupport.execute(() -> {
            boolean flag = menuService.lambdaUpdate().eq(BossMenu::getMenuId, menuId).remove();
            BizAssert.isTrue(flag, BossErrorCode.UPDATE_FAIL);
            //记录日志
            logAssist.operationLog(OperationType.DELETE, OPERATION_BIZ, menuId);
        });

    }

    @Override
    public List<BossMenu> queryAll() {
        return menuService.list();
    }
}
