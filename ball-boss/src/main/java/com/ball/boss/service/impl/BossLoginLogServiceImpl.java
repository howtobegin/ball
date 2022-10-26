package com.ball.boss.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ball.boss.dao.entity.BossLoginLog;
import com.ball.boss.dao.mapper.BossLoginLogMapper;
import com.ball.boss.service.IBossLoginLogService;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 操作日志表 服务实现类
 * </p>
 *
 * @author JimChery
 * @since 2021-09-22
 */
@Service
public class BossLoginLogServiceImpl extends ServiceImpl<BossLoginLogMapper, BossLoginLog> implements IBossLoginLogService {

}
