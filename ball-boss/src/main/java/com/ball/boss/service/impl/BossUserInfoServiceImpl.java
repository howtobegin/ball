package com.ball.boss.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ball.boss.dao.entity.BossUserInfo;
import com.ball.boss.dao.mapper.BossUserInfoMapper;
import com.ball.boss.service.IBossUserInfoService;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 用户信息表 服务实现类
 * </p>
 *
 * @author littlehow
 * @since 2021-09-22
 */
@Service
public class BossUserInfoServiceImpl extends ServiceImpl<BossUserInfoMapper, BossUserInfo> implements IBossUserInfoService {

}
