package com.ball.biz.order.service;

import com.ball.biz.order.entity.OrderHistory;
import com.ball.biz.order.entity.OrderInfo;
import com.baomidou.mybatisplus.extension.service.IService;

import com.ball.common.service.IBaseService;

/**
 * <p>
 * 订单历史表 服务类
 * </p>
 *
 * @author lhl
 * @since 2022-10-26
 */
public interface IOrderHistoryService extends IService<OrderHistory>, IBaseService {

    void saveLatest(OrderInfo order);
}
