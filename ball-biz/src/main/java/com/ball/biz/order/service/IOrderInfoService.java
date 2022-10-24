package com.ball.biz.order.service;

import com.ball.biz.bet.enums.OrderStatus;
import com.ball.biz.order.entity.OrderInfo;
import com.ball.common.service.IBaseService;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 订单表 服务类
 * </p>
 *
 * @author lhl
 * @since 2022-10-20
 */
public interface IOrderInfoService extends IService<OrderInfo>, IBaseService {
    OrderInfo queryByOrderId(String orderId);

    void updateStatus(String orderId, OrderStatus pre, OrderStatus next);

}
