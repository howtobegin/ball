package com.ball.biz.user.service.impl;

import com.ball.biz.user.entity.UserLoginSession;
import com.ball.biz.user.mapper.UserLoginSessionMapper;
import com.ball.biz.user.service.IUserLoginSessionService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 用户session信息管理 服务实现类
 * </p>
 *
 * @author JimChery
 * @since 2022-10-19
 */
@Service
public class UserLoginSessionServiceImpl extends ServiceImpl<UserLoginSessionMapper, UserLoginSession> implements IUserLoginSessionService {

    @Override
    public UserLoginSession getByUid(Long userId) {
        return lambdaQuery().eq(UserLoginSession::getUserId, userId).one();
    }
}
