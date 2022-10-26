package com.ball.boss.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ball.boss.dao.entity.BossCheckLog;
import com.ball.boss.dao.mapper.BossCheckLogMapper;
import com.ball.boss.service.IBossCheckLogService;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 业务审核日志表 服务实现类
 * </p>
 *
 * @author JimChery
 * @since 2021-09-22
 */
@Service
public class BossCheckLogServiceImpl extends ServiceImpl<BossCheckLogMapper, BossCheckLog> implements IBossCheckLogService {

}
