package com.ball.biz.user.assist;

import com.ball.base.model.enums.YesOrNo;
import com.ball.base.util.BizAssert;
import com.ball.base.util.PasswordUtil;
import com.ball.biz.enums.UserTypeEnum;
import com.ball.biz.exception.BizErrCode;
import com.ball.biz.user.entity.UserInfo;
import com.ball.biz.user.entity.UserLoginSession;
import com.ball.biz.user.service.IUserInfoService;
import com.ball.biz.user.service.IUserLoginSessionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * @author littlehow
 */
@Component
public class LoginAssist {
    @Autowired
    private IUserInfoService userInfoService;

    @Autowired
    private IUserLoginSessionService userLoginSessionService;

    @Value("${user.login.single:true}")
    private boolean loginSingle;

    public UserInfo login(String sessionId, String loginAccount, String password, Integer userType) {
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

    private void dealKick(String sessionId, UserLoginSession session) {
        // todo littlehow
    }
}
