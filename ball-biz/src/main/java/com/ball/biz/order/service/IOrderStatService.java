package com.ball.biz.order.service;

import com.ball.biz.order.entity.OrderInfo;
import com.ball.biz.order.entity.OrderStat;
import com.baomidou.mybatisplus.extension.service.IService;

import com.ball.common.service.IBaseService;

import java.time.LocalDate;
import java.util.List;

/**
 * <p>
 * 订单代理商统计表 服务类
 * </p>
 *
 * @author lhl
 * @since 2022-11-01
 */
public interface IOrderStatService extends IService<OrderStat>, IBaseService {

    OrderStat queryOne(LocalDate betDate, Long proxy1, Long proxy2, Long proxy3);

    List<OrderStat> queryByDate(LocalDate start, LocalDate end, Long proxy1, Long proxy2, Long proxy3);

    void addBetCount(LocalDate betDate, Long proxy1, Long proxy2, Long proxy3);

    void newOrderCreate(OrderInfo order);

    void newOrderFinish(OrderInfo order);
}
