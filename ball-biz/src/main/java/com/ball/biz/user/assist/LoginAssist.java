package com.ball.biz.user.assist;

import com.ball.base.context.RequestContext;
import com.ball.base.model.Const;
import com.ball.base.model.enums.YesOrNo;
import com.ball.base.transaction.TransactionSupport;
import com.ball.base.util.BizAssert;
import com.ball.base.util.PasswordUtil;
import com.ball.biz.exception.BizErrCode;
import com.ball.biz.socket.WebsocketManager;
import com.ball.biz.socket.bo.MessageFactory;
import com.ball.biz.user.entity.UserInfo;
import com.ball.biz.user.entity.UserLoginLog;
import com.ball.biz.user.entity.UserLoginSession;
import com.ball.biz.user.service.IUserInfoService;
import com.ball.biz.user.service.IUserLoginLogService;
import com.ball.biz.user.service.IUserLoginSessionService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

/**
 * @author JimChery
 */
@Component
@Slf4j
public class LoginAssist {
    @Autowired
    private IUserInfoService userInfoService;

    @Autowired
    private IUserLoginSessionService userLoginSessionService;

    @Autowired
    private TransactionSupport transactionSupport;

    @Autowired
    private IUserLoginLogService loginLogService;

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @Value("${user.login.single:true}")
    private boolean loginSingle;

    public UserInfo login(String sessionId, String loginAccount, String password, Integer userType, String key1, String key2) {
        UserInfo userInfo = userInfoService.lambdaQuery().eq(UserInfo::getLoginAccount, loginAccount)
                .eq(UserInfo::getUserType, userType)
                .eq(UserInfo::getPassword, PasswordUtil.get(password))
                .one();
        BizAssert.notNull(userInfo, BizErrCode.USER_OR_PASSWORD_ERROR);
        BizAssert.isTrue(YesOrNo.YES.isMe(userInfo.getStatus()), BizErrCode.USER_LOCKED);
        UserLoginSession session = userLoginSessionService.getByUid(userInfo.getId());
        dealLogin(sessionId, session, key1, key2);
        return userInfo;
    }

    public void logout(Long userId, String key1, String key2) {
        UserLoginSession session = userLoginSessionService.getByUid(userId);
        String sessionId = session.getSessionId();
        session.setSessionId(Const.SESSION_DEFAULT)
                .setUpdateTime(null);
        userLoginSessionService.updateById(session);
        // 删除redis key
        if (hasKey(key1 + sessionId)) {
            log.info("logout out userId={}, sessionId={}", userId, sessionId);
            // 删除主要信息key
            redisTemplate.delete(key1 + sessionId);
            redisTemplate.delete(key2 + sessionId);
        }
    }

    private void dealLogin(String sessionId, UserLoginSession session, String key1, String key2) {
        String old = session.getSessionId();
        session.setSessionId(sessionId)
                .setIp(RequestContext.getIp())
                .setUpdateTime(null);
        transactionSupport.execute(() -> {
            userInfoService.lambdaUpdate().set(UserInfo::getLastLogin, System.currentTimeMillis())
                    .eq(UserInfo::getId, session.getUserId())
                    .update();
            userLoginSessionService.updateById(session);
            // 记录登录日志
            loginLogService.save(new UserLoginLog()
                .setIp(RequestContext.getIp()).setSessionId(sessionId)
                    .setStatus(YesOrNo.YES.v).setUserId(session.getUserId())
            );
            // 如果是单点登录则需要踢出之前登录的用户
            if (loginSingle && hasKey(key1 + old)) {
                log.info("kick out userId={}, sessionId={}", old, sessionId);
                // 删除主要信息key
                redisTemplate.delete(key1 + old);
                redisTemplate.delete(key2 + old);
                loginLogService.lambdaUpdate().set(UserLoginLog::getStatus, YesOrNo.NO.v)
                        .set(UserLoginLog::getTerminateIp, RequestContext.getIp())
                        .set(UserLoginLog::getTerminateSid, sessionId)
                        .eq(UserLoginLog::getSessionId, old)
                        .update();
                WebsocketManager.sendMessage(session.getUserId(), old, MessageFactory.getUserKick(RequestContext.getIp()));
            }
        });

    }

    private boolean hasKey(String key) {
        Boolean flag = redisTemplate.hasKey(key);
        return flag == null ? false : flag;
    }
}
