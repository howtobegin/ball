package com.ball.boss.service.system.impl;


import com.ball.base.transaction.TransactionSupport;
import com.ball.base.util.BizAssert;
import com.ball.boss.dao.entity.BossRole;
import com.ball.boss.dao.entity.BossRoleAuthorize;
import com.ball.boss.dao.entity.BossUserAuthorize;
import com.ball.boss.dao.entity.BossUserInfo;
import com.ball.boss.exception.BossErrorCode;
import com.ball.boss.service.IBossRoleAuthorizeService;
import com.ball.boss.service.IBossRoleService;
import com.ball.boss.service.IBossUserAuthorizeService;
import com.ball.boss.service.IBossUserInfoService;
import com.ball.boss.service.system.AuthorizeService;
import com.ball.boss.service.system.LogAssist;
import com.ball.boss.service.system.model.LockEnum;
import com.ball.boss.service.system.model.OperationType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class AuthorizeServiceImpl implements AuthorizeService {
    private static final String AUTHORIZE_LEVEL_ADMIN = "2";

    @Autowired
    private IBossUserInfoService userInfoService;

    @Autowired
    private IBossUserAuthorizeService userAuthorizeService;

    @Autowired
    private IBossRoleAuthorizeService roleAuthorizeService;

    @Autowired
    private IBossRoleService roleService;

    @Autowired
    private TransactionSupport transactionSupport;

    @Autowired
    private LogAssist logAssist;

    @Override
    public void updateUserAuthorize(List<String> roleIds, String userId) {

        log.info("AuthorizeService updateUserAuthorize userId={}, roleIds={}", userId, roleIds);

        //查询用户是否为正常状态
        BossUserInfo userInfo = userInfoService.lambdaQuery().eq(BossUserInfo::getUserId, userId).one();
        BizAssert.notNull(userInfo, BossErrorCode.USER_NOT_EXISTS);
        BizAssert.isTrue(LockEnum.isNotLock(userInfo.getLocked()), BossErrorCode.USER_ACCOUNT_LOCKED);
        transactionSupport.execute(() -> {
            //先删除原有的角色
            userAuthorizeService.lambdaUpdate().eq(BossUserAuthorize::getUserId, userId).remove();
            //插入新的权限
            if (!CollectionUtils.isEmpty(roleIds)) {
                boolean flag = userAuthorizeService.saveBatch(
                        roleIds.stream().map(roleId -> new BossUserAuthorize()
                                .setRoleId(roleId)
                                .setUserId(userId))
                                .collect(Collectors.toList()));
                BizAssert.isTrue(flag, BossErrorCode.UPDATE_FAIL);
            }
            logAssist.operationLog(OperationType.UPDATE, "user_authorize", userId, "bizId为用户编号");
        });

    }

    @Override
    public void updateRoleAuthorize(List<String> menuIds, String roleId) {
        log.info("AuthorizeService updateRoleAuthorize roleId={}, menuIds={}", roleId, menuIds);
        BossRole role = roleService.lambdaQuery().eq(BossRole::getRoleId, roleId).one();
        BizAssert.notNull(role, BossErrorCode.DATA_NOT_EXISTS);
        BizAssert.isTrue(LockEnum.isNotLock(role.getLocked()), BossErrorCode.ROLE_LOCKED);
        transactionSupport.execute(() -> {
            //先删除原有菜单信息
            roleAuthorizeService.lambdaUpdate().eq(BossRoleAuthorize::getRoleId, roleId).remove();
            //插入菜单
            if (!CollectionUtils.isEmpty(menuIds)) {
                boolean flag = roleAuthorizeService.saveBatch(
                        menuIds.stream().map(menuId -> new BossRoleAuthorize()
                                .setAuthorizeLevel(AUTHORIZE_LEVEL_ADMIN)
                                .setMenuId(menuId)
                                .setRoleId(roleId))
                                .collect(Collectors.toList()));
                BizAssert.isTrue(flag, BossErrorCode.UPDATE_FAIL);
            }
            logAssist.operationLog(OperationType.UPDATE, "role_authorize", roleId, "bizId为角色编号");
        });

    }
}
