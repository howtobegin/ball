package com.ball.biz.match.service.impl;

import com.ball.biz.match.entity.Players;
import com.ball.biz.match.mapper.PlayersMapper;
import com.ball.biz.match.service.IPlayersService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author lhl
 * @since 2022-10-19
 */
@Service
public class PlayersServiceImpl extends ServiceImpl<PlayersMapper, Players> implements IPlayersService {

}
