package com.ball.biz.user.service.impl;

import com.ball.base.model.enums.YesOrNo;
import com.ball.base.util.BizAssert;
import com.ball.biz.exception.BizErrCode;
import com.ball.biz.user.entity.UserLoginLog;
import com.ball.biz.user.mapper.UserLoginLogMapper;
import com.ball.biz.user.service.IUserLoginLogService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;

/**
 * <p>
 * 用户登录日志 服务实现类
 * </p>
 *
 * @author littlehow
 * @since 2022-10-19
 */
@Service
public class UserLoginLogServiceImpl extends ServiceImpl<UserLoginLogMapper, UserLoginLog> implements IUserLoginLogService {

    @Override
    public void checkKickOut(String sessionId) {
        List<UserLoginLog> log = lambdaQuery().eq(UserLoginLog::getSessionId, sessionId).list();
        if (CollectionUtils.isEmpty(log)) {
            return;
        }
        UserLoginLog loginLog = log.get(0);
        BizAssert.isTrue(YesOrNo.YES.isMe(loginLog.getStatus()), BizErrCode.USER_LOGIN_KICK_OUT);
    }
}
