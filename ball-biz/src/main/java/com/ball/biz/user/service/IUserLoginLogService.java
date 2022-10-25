package com.ball.biz.user.service;

import com.ball.biz.user.entity.UserLoginLog;
import com.baomidou.mybatisplus.extension.service.IService;

import com.ball.common.service.IBaseService;

/**
 * <p>
 * 用户登录日志 服务类
 * </p>
 *
 * @author littlehow
 * @since 2022-10-19
 */
public interface IUserLoginLogService extends IService<UserLoginLog>, IBaseService {
    /**
     * 检查登录信息
     * @param sessionId - sessionId
     */
    void checkKickOut(String sessionId);

    /**
     * 用户用户编号
     * @param sessionId  -
     * @return -
     */
    Long getBySessionId(String sessionId);
}
