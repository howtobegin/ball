package com.ball.boss.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ball.boss.dao.entity.BossRole;
import com.ball.boss.dao.mapper.BossRoleMapper;
import com.ball.boss.service.IBossRoleService;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 角色信息表 服务实现类
 * </p>
 *
 * @author littlehow
 * @since 2021-09-22
 */
@Service
public class BossRoleServiceImpl extends ServiceImpl<BossRoleMapper, BossRole> implements IBossRoleService {

}
