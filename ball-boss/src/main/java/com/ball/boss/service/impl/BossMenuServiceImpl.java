package com.ball.boss.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ball.boss.dao.entity.BossMenu;
import com.ball.boss.dao.mapper.BossMenuMapper;
import com.ball.boss.service.IBossMenuService;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 菜单资源信息表 服务实现类
 * </p>
 *
 * @author littlehow
 * @since 2021-09-22
 */
@Service
public class BossMenuServiceImpl extends ServiceImpl<BossMenuMapper, BossMenu> implements IBossMenuService {

}
