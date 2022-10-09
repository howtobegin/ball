package com.ball.boss.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ball.boss.dao.entity.BossCode;
import com.ball.boss.dao.mapper.BossCodeMapper;
import com.ball.boss.service.IBossCodeService;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 代码对照表 服务实现类
 * </p>
 *
 * @author littlehow
 * @since 2021-09-22
 */
@Service
public class BossCodeServiceImpl extends ServiceImpl<BossCodeMapper, BossCode> implements IBossCodeService {

}
