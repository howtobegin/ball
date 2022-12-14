package com.ball.biz.order.service;

import com.ball.biz.order.bo.OrderStatUniqBo;
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

    void init();

    OrderStat queryOne(OrderStatUniqBo uniqBo);

    OrderStatUniqBo buildUniqWhere(OrderInfo order);

    List<OrderStat> queryByDate(LocalDate start, LocalDate end, Long proxy1, Long proxy2, Long proxy3);

    void newOrderCreate(OrderInfo order);

    void newOrderFinish(OrderInfo order);

    OrderStat buildOrderStat(OrderInfo order);

    OrderStat buildOrderStat(OrderInfo order, Long betCount);

    /**
     * 根据日期，代理分组求和
     */
    OrderStat sumRmbByDateAndProxy(LocalDate start, LocalDate end, Long proxy1, Long proxy2, Long proxy3);

    List<OrderStat> sumRmbGroupByDate(LocalDate start, LocalDate end, Long proxy1, Long proxy2, Long proxy3);

    /**
     * 按代理分组求和
     *
     * @param start
     * @param end
     * @param proxy1    不能为空
     * @param proxy2
     * @param proxy3
     * @param level     报表层级 1或2
     * @return
     */
    List<OrderStat> sumRmbGroupByProxy(LocalDate start, LocalDate end, Long proxy1, Long proxy2, Long proxy3, int level);

    /**
     * 参数都不能为空
     * @param start
     * @param end
     * @param proxy1
     * @param proxy2
     * @param proxy3
     * @return
     */
    List<OrderStat> sumRmbGroupByUser(LocalDate start, LocalDate end, Long proxy1, Long proxy2, Long proxy3);
}
