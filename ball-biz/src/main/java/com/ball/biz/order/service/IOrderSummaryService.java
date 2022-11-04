package com.ball.biz.order.service;

import com.ball.biz.order.entity.OrderInfo;
import com.ball.biz.order.entity.OrderSummary;
import com.ball.common.service.IBaseService;
import com.baomidou.mybatisplus.extension.service.IService;

import java.time.LocalDate;
import java.util.List;

/**
 * <p>
 * 赛事结果概要 服务类
 * </p>
 *
 * @author lhl
 * @since 2022-11-01
 */
public interface IOrderSummaryService extends IService<OrderSummary>, IBaseService {

    OrderSummary queryOne(LocalDate date, Integer sport);

    List<OrderSummary> queryByDate(LocalDate date);

    void newOrderCreate(OrderInfo order);

    void newOrderFinish(OrderInfo order);
}
