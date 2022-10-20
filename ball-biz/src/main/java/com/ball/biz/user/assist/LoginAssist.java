package com.ball.biz.user.assist;

import com.ball.base.context.RequestContext;
import com.ball.base.model.Const;
import com.ball.base.model.enums.YesOrNo;
import com.ball.base.util.BizAssert;
import com.ball.base.util.PasswordUtil;
import com.ball.biz.enums.UserTypeEnum;
import com.ball.biz.exception.BizErrCode;
import com.ball.biz.user.entity.UserInfo;
import com.ball.biz.user.entity.UserLoginSession;
import com.ball.biz.user.service.IUserInfoService;
import com.ball.biz.user.service.IUserLoginSessionService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

/**
 * @author littlehow
 */
@Component
@Slf4j
public class LoginAssist {
    @Autowired
    private IUserInfoService userInfoService;

    @Autowired
    private IUserLoginSessionService userLoginSessionService;

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
        dealKick(sessionId, session);
        return userInfo;
    }

    public void logout(Long userId, String key1, String key2) {
        UserLoginSession session = userLoginSessionService.getByUid(userId);
        String sessionId = session.getSessionId();
        session.setSessionId(Const.SESSION_DEFAULT)
                .setUpdateTime(null);
        userLoginSessionService.updateById(session);
        // 删除redis key
        if (redisTemplate.hasKey(key1 + sessionId)) {
            log.info("logout out userId={}, sessionId={}", userId, sessionId);
            // 删除主要信息key
            redisTemplate.delete(key1 + sessionId);
            redisTemplate.delete(key2 + sessionId);
        }
    }

    private void dealKick(String sessionId, UserLoginSession session) {
        // todo littlehow
        session.setSessionId(sessionId)
                .setIp(RequestContext.getIp())
                .setUpdateTime(null);
        userLoginSessionService.updateById(session);
    }
}
