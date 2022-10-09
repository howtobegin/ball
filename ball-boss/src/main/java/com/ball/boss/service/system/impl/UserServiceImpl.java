package com.ball.boss.service.system.impl;



import com.ball.base.model.PageResult;
import com.ball.base.util.BizAssert;
import com.ball.base.util.SaltBase64Util;
import com.ball.boss.dao.entity.*;
import com.ball.boss.dao.ext.PrivilegeMapper;
import com.ball.boss.exception.BossErrorCode;
import com.ball.boss.service.*;
import com.ball.boss.service.system.BaseService;
import com.ball.boss.service.system.UserService;
import com.ball.boss.service.system.model.LockEnum;
import com.ball.boss.service.system.model.OperationType;
import com.ball.boss.service.system.model.RoleMenuInfo;
import com.ball.boss.service.system.model.UserType;
import com.ball.common.service.GoogleAuthService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.stream.Collectors;

import static com.ball.base.util.EncryptUtil.md5Plus;


@Service
@Slf4j
public class UserServiceImpl extends BaseService implements UserService {
    private static final String OPERATION_BIZ = "boss_user_info";

    private static final AntPathMatcher ANT_PATH_MATCHER = new AntPathMatcher();

    @Autowired
    private PrivilegeMapper privilegeMapper;

    @Autowired
    private IBossUserAuthorizeService userAuthorizeService;

    @Autowired
    private IBossUserInfoService userInfoService;

    @Autowired
    private IBossLoginSessionService loginSessionService;

    @Autowired
    private IBossMenuService bossMenuService;

    @Autowired
    private IBossRoleService bossRoleService;

    @Autowired
    private GoogleAuthService googleAuthService;

    /**
     * 脱敏密码
     *
     * @param password -- 密码
     * @return
     */
    private static String desensitizationPassword(String password) {
        //第2345个字符隐藏({3,4}是为了防止密码总长度为4位的时候的脱敏)
        return password.replaceAll("^(.).{3,4}", "$1****");
    }

    @Override
    public RoleMenuInfo getUserRoleMenuInfo(String userId) {
        log.info("UserService getUserRoleMenuInfo userId={}", userId);
        //获取用户角色
        List<BossRole> roles = privilegeMapper.queryRoleByUserId(userId);
        BizAssert.notEmpty(roles, BossErrorCode.USER_NOT_ROLE);
        //根据角色获取权限信息
        List<BossMenu> menus =
                privilegeMapper.queryMenuByRoleIds(roles.stream().map(BossRole::getRoleId).collect(Collectors.toList()));
        BizAssert.notEmpty(menus, BossErrorCode.USER_NOT_MENU);
        return RoleMenuInfo.builder()
                .menus(menus)
                .roles(roles)
                .build();
    }

    @Override
    public List<BossRole> getUserRoles(String userId) {
        log.info("UserService getUserRoles userId={}", userId);
        return privilegeMapper.queryRoleByUserId(userId);
    }

    @Override
    public String insert(BossUserInfo userInfo) {
        // 先查询账户是否已经存在
        int count = userInfoService.lambdaQuery().eq(BossUserInfo::getAccount, userInfo.getAccount()).count();
        BizAssert.isZero(count, BossErrorCode.DATA_NOT_EXISTS, userInfo.getAccount());
        userInfo.setDeptId(DEPT_ID_DEFAULT);
        userInfo.setUserId(getBizId());
        userInfo.setPassword(md5Plus(userInfo.getPassword()));
        log.info("UserService insert userInfo={}", toJsonString(userInfo));
        transactionSupport.execute(() -> {
            userInfoService.save(userInfo);
            // 插入登录session初始信息，便于后续登录只需做修改操作
            loginSessionService.save(new BossLoginSession()
                    .setUserId(userInfo.getUserId())
                    .setSessionId(DEPT_ID_DEFAULT));
            logAssist.operationLog(OperationType.ADD, OPERATION_BIZ, userInfo.getUserId());
        });

        return userInfo.getUserId();
    }

    @Override
    public void update(BossUserInfo userInfo) {
        log.info("UserService update userInfo={}", toJsonString(userInfo));
        BossUserInfo dbUser = userInfoService.lambdaQuery().eq(BossUserInfo::getUserId, userInfo.getUserId()).one();
        validOperator(dbUser);
        userInfo.setId(dbUser.getId());
        transactionSupport.execute(() -> {
            userInfoService.updateById(userInfo);
            logAssist.operationLog(OperationType.UPDATE, OPERATION_BIZ, userInfo.getUserId());
        });
    }

    @Override
    public void delete(String userId) {
        log.info("UserService delete userId={}", userId);
        BossUserInfo dbUser = userInfoService.lambdaQuery().eq(BossUserInfo::getUserId, userId).one();
        validOperator(dbUser);
        transactionSupport.execute(() -> {
            userInfoService.lambdaUpdate().eq(BossUserInfo::getUserId, userId).remove();
            // 删除权限映射关系
            userAuthorizeService.lambdaUpdate().eq(BossUserAuthorize::getUserId, userId).remove();
            logAssist.operationLog(OperationType.DELETE, OPERATION_BIZ, userId);
        });

    }

    @Override
    public void changePassword(String oldPassword, String newPassword) {
        log.info("UserService changePassword oldPassword={}, newPassword={}", oldPassword, newPassword);
        BossUserInfo userInfo = userInfoService.lambdaQuery().eq(BossUserInfo::getUserId, getLoginUserId()).one();
        BizAssert.notNull(userInfo, BossErrorCode.DATA_NOT_EXISTS);
        BizAssert.isTrue(LockEnum.isNotLock(userInfo.getLocked()), BossErrorCode.USER_ACCOUNT_LOCKED);
        BizAssert.isTrue(userInfo.getPassword().equals(md5Plus(oldPassword)), BossErrorCode.ORIGIN_PASSWORD_ERROR);
        userInfo.setPassword(md5Plus(newPassword));
        userInfo.setUpdateTime(null);
        transactionSupport.execute(() -> {
            boolean flag = userInfoService.updateById(userInfo);
            BizAssert.isTrue(flag, BossErrorCode.UPDATE_FAIL);
            logAssist.operationLog(OperationType.UPDATE, OPERATION_BIZ, userInfo.getUserId(),
                    "修改密码old=" + desensitizationPassword(oldPassword) + ",new=" + desensitizationPassword(newPassword));
        });
    }

    @Override
    public void changeLock(String userId, String locked) {
        log.info("UserService changeLock userId={}, locked={}", userId, locked);
        //先查询
        BossUserInfo dbUser = userInfoService.lambdaQuery().eq(BossUserInfo::getUserId, userId).one();
        BizAssert.notNull(dbUser, BossErrorCode.DATA_NOT_EXISTS);
        if (dbUser.getLocked().equals(locked)) {
            return;
        }
        transactionSupport.execute(() -> {
            dbUser.setUpdateTime(null);
            dbUser.setLocked(locked);
            boolean flag = userInfoService.updateById(dbUser);
            BizAssert.isTrue(flag, BossErrorCode.UPDATE_FAIL);
            //记录日志
            logAssist.operationLog(OperationType.UPDATE, OPERATION_BIZ, userId, "locked=" + locked);
        });

    }

    @Override
    public BossUserInfo queryById(String userId) {
        log.info("UserService queryById userId={}", userId);
        //先查询
        BossUserInfo dbUser = userInfoService.lambdaQuery().eq(BossUserInfo::getUserId, userId).one();
        BizAssert.notNull(dbUser, BossErrorCode.DATA_NOT_EXISTS);
        return dbUser;
    }

    @Override
    public BossUserInfo queryByAccountAndPassword(String accountId, String password) {
        //先查询
        return userInfoService.lambdaQuery().eq(BossUserInfo::getAccount, accountId)
                .eq(BossUserInfo::getPassword, password)
                .one();
    }

    @Override
    public PageResult<BossUserInfo> queryPaging(BossUserInfo userInfo) {
        if (userInfo.getUserName() != null) {
            userInfo.setUserName("%" + userInfo.getUserName() + "%");
        }
        return pageQuery(userInfoService.lambdaQuery()
                    .eq(userInfo.getMobilePhone() != null, BossUserInfo::getMobilePhone, userInfo.getMobilePhone())
                    .eq(userInfo.getAccount() != null, BossUserInfo::getAccount, userInfo.getAccount())
                    .like(userInfo.getUserName() != null, BossUserInfo::getUserName, userInfo.getUserName())
                , userInfo);
    }

    @Override
    public boolean checkPermission(String userId, String uri) {
        return getUserRoleMenuInfo(userId)
                .getMenus().stream()
                .map(BossMenu::getRequest)
                .filter(StringUtils::hasText)
                // 请求 url与 MenuPo.getRequest 匹配
                .anyMatch(requestPattern -> ANT_PATH_MATCHER.match(requestPattern, uri));
    }

    @Override
    public void bindGoogle(String account, String code) {
       String secret = googleAuthService.getGoogleSecretBoss(account);
       googleAuthService.checkGoogleCode(secret, code);
       transactionSupport.execute(() -> {
           userInfoService.lambdaUpdate().set(BossUserInfo::getGoogleKey, SaltBase64Util.encode(secret))
                   .eq(BossUserInfo::getAccount, account)
                   .update();
           logAssist.operationLog(OperationType.UPDATE, OPERATION_BIZ, account, "设置google验证码:" +
                   SaltBase64Util.encode(SaltBase64Util.encode(secret)));
       });
    }

    @Override
    public void unbindGoogle(String account, String code) {
        BossUserInfo userInfo = userInfoService.lambdaQuery().eq(BossUserInfo::getAccount, account).one();
        if (StringUtils.hasText(userInfo.getGoogleKey())) {
            googleAuthService.checkGoogleCode(SaltBase64Util.decode(userInfo.getGoogleKey()), code);
            transactionSupport.execute(() -> {
                userInfoService.lambdaUpdate().set(BossUserInfo::getGoogleKey, null)
                        .eq(BossUserInfo::getAccount, account)
                        .update();
                logAssist.operationLog(OperationType.UPDATE, OPERATION_BIZ, account, "取消google验证码");
            });
        }
    }

    /**
     * 验证用户操作权限
     *
     * @param user
     */
    private void validOperator(BossUserInfo user) {
        BizAssert.notNull(user, BossErrorCode.DATA_NOT_EXISTS);
        //首先查询操作员(系统内置人员可以删除管理员,管理员可以删除业务员)
        BossUserInfo operationUser = userInfoService.lambdaQuery().eq(BossUserInfo::getUserId, getLoginUserId()).one();
        BizAssert.notNull(operationUser, BossErrorCode.DATA_NOT_EXISTS);
        BizAssert.isTrue(LockEnum.isNotLock(operationUser.getLocked()), BossErrorCode.USER_ACCOUNT_LOCKED);
        //判断操作员权限是否大于被操作人
        UserType operationUserType = UserType.value(operationUser.getUserType());
        UserType userType = UserType.value(user.getUserType());
        BizAssert.isTrue(operationUserType.biggerThan(userType), BossErrorCode.NOT_PERMISSION);
    }

}
