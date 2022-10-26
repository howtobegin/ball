package com.ball.biz.order.service.impl;

import com.ball.base.util.BeanUtil;
import com.ball.biz.order.entity.OrderHistory;
import com.ball.biz.order.entity.OrderInfo;
import com.ball.biz.order.mapper.OrderHistoryMapper;
import com.ball.biz.order.service.IOrderHistoryService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 订单历史表 服务实现类
 * </p>
 *
 * @author lhl
 * @since 2022-10-26
 */
@Service
public class OrderHistoryServiceImpl extends ServiceImpl<OrderHistoryMapper, OrderHistory> implements IOrderHistoryService {

    @Override
    public void saveLatest(OrderInfo order) {
        OrderHistory history = BeanUtil.copy(order, OrderHistory.class);
        history.setId(null);
        history.setCreateTime(null);
        history.setUpdateTime(null);

        save(history);
    }
}
