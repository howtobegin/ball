package com.ball.biz.order.service.impl;

import com.ball.biz.order.entity.OrderStat;
import com.ball.biz.order.mapper.OrderStatMapper;
import com.ball.biz.order.service.IOrderStatService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 订单代理商统计表 服务实现类
 * </p>
 *
 * @author lhl
 * @since 2022-11-01
 */
@Service
public class OrderStatServiceImpl extends ServiceImpl<OrderStatMapper, OrderStat> implements IOrderStatService {

}
