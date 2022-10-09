package com.ball.boss.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ball.boss.dao.entity.BossOperationLog;
import com.ball.boss.dao.mapper.BossOperationLogMapper;
import com.ball.boss.service.IBossOperationLogService;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 操作日志表 服务实现类
 * </p>
 *
 * @author littlehow
 * @since 2021-09-22
 */
@Service
public class BossOperationLogServiceImpl extends ServiceImpl<BossOperationLogMapper, BossOperationLog> implements IBossOperationLogService {

}
