package com.ball.boss.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ball.boss.dao.entity.BossLoginSession;
import com.ball.boss.dao.mapper.BossLoginSessionMapper;
import com.ball.boss.service.IBossLoginSessionService;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 用户登录信息表 服务实现类
 * </p>
 *
 * @author JimChery
 * @since 2021-09-22
 */
@Service
public class BossLoginSessionServiceImpl extends ServiceImpl<BossLoginSessionMapper, BossLoginSession> implements IBossLoginSessionService {

}
