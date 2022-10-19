package com.ball.biz.user.service;

import com.ball.biz.user.entity.UserLoginSession;
import com.baomidou.mybatisplus.extension.service.IService;

import com.ball.common.service.IBaseService;

/**
 * <p>
 * 用户session信息管理 服务类
 * </p>
 *
 * @author littlehow
 * @since 2022-10-19
 */
public interface IUserLoginSessionService extends IService<UserLoginSession>, IBaseService {
    UserLoginSession getByUid(Long userId);

}
