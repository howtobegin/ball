package com.ball.biz.user.service.impl;

import com.ball.base.model.Const;
import com.ball.base.model.enums.YesOrNo;
import com.ball.base.transaction.TransactionSupport;
import com.ball.base.util.BizAssert;
import com.ball.base.util.PasswordUtil;
import com.ball.biz.base.service.IIdGenService;
import com.ball.biz.base.service.TableNameEnum;
import com.ball.biz.enums.UserTypeEnum;
import com.ball.biz.exception.BizErrCode;
import com.ball.biz.user.entity.UserInfo;
import com.ball.biz.user.entity.UserLoginSession;
import com.ball.biz.user.mapper.UserInfoMapper;
import com.ball.biz.user.service.IUserInfoService;
import com.ball.biz.user.service.IUserLoginSessionService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 用户信息表 服务实现类
 * </p>
 *
 * @author littlehow
 * @since 2022-10-19
 */
@Service
@Slf4j
public class UserInfoServiceImpl extends ServiceImpl<UserInfoMapper, UserInfo> implements IUserInfoService {

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @Autowired
    private TransactionSupport transactionSupport;

    @Autowired
    private IUserLoginSessionService userLoginSessionService;

    @Autowired
    private IIdGenService idGenService;

    @Override
    public Long addUser(String account, String userName, String password, String proxyAccount, Long proxyUid) {
        // 判断用户是否存在
        UserInfo userInfo = getByAccount(account);
        BizAssert.isNull(userInfo, BizErrCode.USER_EXISTS);
        Long userId = idGenService.get(TableNameEnum.USER_INFO);
        transactionSupport.execute(() -> {
            save(new UserInfo().setId(userId)
                .setAccount(account).setChangePasswordFlag(YesOrNo.NO.v)
                    .setLoginAccount(account).setPassword(PasswordUtil.get(password))
                    .setProxyAccount(proxyAccount).setStatus(YesOrNo.YES.v)
                    .setUserName(userName).setUserType(UserTypeEnum.GENERAL.v)
                    .setProxyUserId(proxyUid)
            );
            userLoginSessionService.save(new UserLoginSession()
                .setUserId(userId).setSessionId(Const.SESSION_DEFAULT)
            );
        });
        return userId;
    }

    @Override
    public UserInfo getByUid(Long userId) {
        return lambdaQuery().eq(UserInfo::getId, userId).one();
    }

    @Override
    public UserInfo getByAccount(String account) {
        return lambdaQuery().eq(UserInfo::getAccount, account)
                .eq(UserInfo::getUserType, UserTypeEnum.GENERAL.v)
                .one();
    }

    @Override
    public UserInfo getByLogin(String loginAccount) {
        return lambdaQuery().eq(UserInfo::getLoginAccount, loginAccount)
                .eq(UserInfo::getUserType, UserTypeEnum.GENERAL.v)
                .one();
    }

    @Override
    public void changePassword(Long userId, String oldPassword, String newPassword) {
        UserInfo userInfo = getByUid(userId);
        BizAssert.isTrue(PasswordUtil.checkPass(oldPassword, userInfo.getPassword()), BizErrCode.USER_PASSWORD_ERROR);
        userInfo.setChangePasswordFlag(YesOrNo.YES.v)
                .setPassword(PasswordUtil.get(newPassword))
                .setUpdateTime(null);
    }

    @Override
    public void changeLogin(Long userId, String loginAccount) {
        UserInfo userInfo = getByUid(userId);
        BizAssert.isTrue(userInfo.getAccount().equals(userInfo.getLoginAccount()), BizErrCode.DATA_ERROR);
        UserInfo exists = getByLogin(loginAccount);
        BizAssert.isNull(exists, BizErrCode.USER_EXISTS);
        exists = getByAccount(loginAccount);
        BizAssert.isNull(exists, BizErrCode.USER_EXISTS);
        lambdaUpdate().set(UserInfo::getLoginAccount, loginAccount)
                .eq(UserInfo::getId, userId)
                .update();
    }

    @Override
    public void setLockPassword(Long userId, String lockPassword) {
        lambdaUpdate().set(UserInfo::getLockPassword, PasswordUtil.get(lockPassword))
                .eq(UserInfo::getId, userId)
                .update();
    }

    @Override
    public void clearLockPassword(Long userId) {
        lambdaUpdate().set(UserInfo::getLockPassword, null)
                .eq(UserInfo::getId, userId)
                .update();
    }

    @Override
    public UserInfo login(String sessionId, String loginAccount, String password) {
        UserInfo userInfo = lambdaQuery().eq(UserInfo::getLoginAccount, loginAccount)
                .eq(UserInfo::getUserType, UserTypeEnum.GENERAL.v)
                .eq(UserInfo::getPassword, PasswordUtil.get(password))
                .one();
        BizAssert.notNull(userInfo, BizErrCode.USER_OR_PASSWORD_ERROR);
        BizAssert.isTrue(YesOrNo.YES.isMe(userInfo.getStatus()), BizErrCode.USER_LOCKED);
        UserLoginSession session = userLoginSessionService.getByUid(userInfo.getId());
        dealKick(sessionId, session);
        return userInfo;
    }

    @Override
    public void logout(Long userId) {
        UserLoginSession session = userLoginSessionService.getByUid(userId);
        String sessionId = session.getSessionId();
        session.setSessionId(Const.SESSION_DEFAULT)
                .setUpdateTime(null);
        userLoginSessionService.updateById(session);
        // 删除redis key
        if (redisTemplate.hasKey(SPRING_SESSION_KEY_PREFIX + sessionId)) {
            log.info("logout out userId={}, sessionId={}", userId, sessionId);
            // 删除主要信息key
            redisTemplate.delete(SPRING_SESSION_KEY_PREFIX + sessionId);
            redisTemplate.delete(SPRING_SESSION_EXPIRE_PREFIX + sessionId);
        }
    }

    private void dealKick(String sessionId, UserLoginSession session) {
        // todo littlehow
    }
}
