package com.ball.biz.user.service.impl;

import com.ball.biz.user.entity.UserExtend;
import com.ball.biz.user.mapper.UserExtendMapper;
import com.ball.biz.user.service.IUserExtendService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 用户扩展信息 服务实现类
 * </p>
 *
 * @author JimChery
 * @since 2022-10-26
 */
@Service
public class UserExtendServiceImpl extends ServiceImpl<UserExtendMapper, UserExtend> implements IUserExtendService {

    @Override
    public UserExtend getByUid(Long userId) {
        return lambdaQuery().eq(UserExtend::getId, userId).one();
    }

    @Override
    public List<UserExtend> getByUid(List<Long> uid) {
        return lambdaQuery().in(UserExtend::getId, uid).list();
    }
}
