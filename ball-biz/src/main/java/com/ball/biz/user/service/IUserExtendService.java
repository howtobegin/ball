package com.ball.biz.user.service;

import com.ball.biz.user.entity.UserExtend;
import com.baomidou.mybatisplus.extension.service.IService;

import com.ball.common.service.IBaseService;

import java.util.List;

/**
 * <p>
 * 用户扩展信息 服务类
 * </p>
 *
 * @author JimChery
 * @since 2022-10-26
 */
public interface IUserExtendService extends IService<UserExtend>, IBaseService {
    UserExtend getByUid(Long userId);

    List<UserExtend> getByUid(List<Long> uid);
}
