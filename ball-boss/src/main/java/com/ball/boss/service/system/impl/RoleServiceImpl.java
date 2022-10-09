package com.ball.boss.service.system.impl;



import com.ball.base.model.PageResult;
import com.ball.base.util.BizAssert;
import com.ball.boss.dao.entity.BossMenu;
import com.ball.boss.dao.entity.BossRole;
import com.ball.boss.dao.entity.BossRoleAuthorize;
import com.ball.boss.dao.entity.BossUserAuthorize;
import com.ball.boss.dao.ext.PrivilegeMapper;
import com.ball.boss.exception.BossErrorCode;
import com.ball.boss.service.IBossRoleAuthorizeService;
import com.ball.boss.service.IBossRoleService;
import com.ball.boss.service.IBossUserAuthorizeService;
import com.ball.boss.service.system.BaseService;
import com.ball.boss.service.system.RoleService;
import com.ball.boss.service.system.model.OperationType;
import com.ball.boss.service.system.model.RoleMenuInfo;
import com.ball.boss.service.system.model.RoleType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;


@Slf4j
@Service
public class RoleServiceImpl extends BaseService implements RoleService {
    private static final String OPERATION_BIZ = "role";
    @Autowired
    private PrivilegeMapper privilegeMapper;

    @Autowired
    private IBossRoleService roleService;

    @Autowired
    private IBossUserAuthorizeService userAuthorizeService;

    @Autowired
    private IBossRoleAuthorizeService roleAuthorizeService;


    @Override
    public String insert(BossRole role) {
        role.setRoleId(getBizId());
        log.info("RoleService insert role={}", toJsonString(role));
        transactionSupport.execute(() -> {
            roleService.save(role);
            //记录日志
            logAssist.operationLog(OperationType.ADD, OPERATION_BIZ, role.getRoleId());
        });

        return role.getRoleId();
    }

    @Override
    public void update(BossRole role) {
        log.info("RoleService update role={}", toJsonString(role));
        //先查询
        BossRole dbRole = roleService.lambdaQuery().eq(BossRole::getRoleId, role.getRoleId()).one();
        BizAssert.notNull(dbRole, BossErrorCode.DATA_NOT_EXISTS);
        dbRole.setUpdateTime(null);
        dbRole.setRemark(role.getRemark());
        dbRole.setRoleName(role.getRoleName());
        dbRole.setRoleType(role.getRoleType());
        transactionSupport.execute(() -> {
            boolean flag = roleService.updateById(dbRole);
            BizAssert.isTrue(flag, BossErrorCode.UPDATE_FAIL);
            //记录日志
            logAssist.operationLog(OperationType.UPDATE, OPERATION_BIZ, role.getRoleId());
        });

    }

    @Override
    public void delete(String roleId) {
        log.info("RoleService delete roleId={}", roleId);
        //先查询
        BossRole dbRole = roleService.lambdaQuery().eq(BossRole::getRoleId, roleId).one();
        BizAssert.notNull(dbRole, BossErrorCode.DATA_NOT_EXISTS);
        BizAssert.isTrue(!RoleType.isSystem(dbRole.getRoleType()), BossErrorCode.DATA_NOT_EDITABLE);
        //判断是否已经将角色分配给人
        int count = userAuthorizeService.lambdaQuery().eq(BossUserAuthorize::getRoleId, roleId).count();
        BizAssert.isZero(count, BossErrorCode.HAS_OCCUPIED);
        transactionSupport.execute(() -> {
            boolean flag = roleService.lambdaUpdate().eq(BossRole::getRoleId, roleId).remove();
            BizAssert.isTrue(flag, BossErrorCode.UPDATE_FAIL);
            //删除角色与权限的关系
            roleAuthorizeService.lambdaUpdate().eq(BossRoleAuthorize::getRoleId, roleId).remove();
            //记录日志
            logAssist.operationLog(OperationType.DELETE, OPERATION_BIZ, roleId);
        });

    }

    @Override
    public void lockOrNot(String roleId, String lockFlag) {
        log.info("RoleService lockOrNot roleId={}, lockFlag={}", roleId, lockFlag);
        //先查询
        BossRole dbRole = roleService.lambdaQuery().eq(BossRole::getRoleId, roleId).one();
        BizAssert.notNull(dbRole, BossErrorCode.DATA_NOT_EXISTS);
        if (dbRole.getLocked().equals(lockFlag)) {
            return;
        }
        dbRole.setUpdateTime(null);
        dbRole.setLocked(lockFlag);
        transactionSupport.execute(() -> {
            boolean flag = roleService.updateById(dbRole);
            BizAssert.isTrue(flag, BossErrorCode.UPDATE_FAIL);
            //记录日志
            logAssist.operationLog(OperationType.UPDATE, OPERATION_BIZ, roleId, "locked=" + lockFlag);
        });

    }

    @Override
    public List<BossRole> queryAll() {
        return roleService.list();
    }

    @Override
    public PageResult<BossRole> queryPaging(BossRole rolePo) {
        if (rolePo.getRoleName() != null) {
            rolePo.setRoleName("%" + rolePo.getRoleName() + "%");
        }
        if (rolePo.getRemark() != null) {
            rolePo.setRemark("%" + rolePo.getRemark() + "%");
        }
        return pageQuery(roleService.lambdaQuery()
                .eq(rolePo.getRoleId() != null, BossRole::getRoleId, rolePo.getRoleId())
                .like(rolePo.getRoleName() != null, BossRole::getRoleName, rolePo.getRoleName())
                .like(rolePo.getRemark() != null, BossRole::getRemark, rolePo.getRemark()),
                rolePo);
    }

    @Override
    public BossRole queryById(String roleId) {
        log.info("RoleService queryById roleId={}", roleId);
        BossRole role = roleService.lambdaQuery().eq(BossRole::getRoleId, roleId).one();
        BizAssert.notNull(role, BossErrorCode.DATA_NOT_EXISTS);
        return role;
    }

    @Override
    public RoleMenuInfo queryDetail(String roleId) {
        log.info("RoleService queryDetail roleId={}", roleId);
        BossRole role = roleService.lambdaQuery().eq(BossRole::getRoleId, roleId).one();
        BizAssert.notNull(role, BossErrorCode.DATA_NOT_EXISTS);
        List<BossMenu> menus = privilegeMapper.queryMenuByRoleIds(Collections.singletonList(roleId));
        return RoleMenuInfo.builder().roles(Collections.singletonList(role)).menus(menus).build();
    }

}
