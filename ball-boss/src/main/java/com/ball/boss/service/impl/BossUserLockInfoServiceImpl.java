package com.ball.boss.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ball.boss.dao.entity.BossUserLockInfo;
import com.ball.boss.dao.mapper.BossUserLockInfoMapper;
import com.ball.boss.service.IBossUserLockInfoService;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 锁定信息 服务实现类
 * </p>
 *
 * @author JimChery
 * @since 2021-09-22
 */
@Service
public class BossUserLockInfoServiceImpl extends ServiceImpl<BossUserLockInfoMapper, BossUserLockInfo> implements IBossUserLockInfoService {

}
