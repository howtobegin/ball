package com.ball.biz.order.service;

import com.ball.base.model.PageResult;
import com.ball.biz.bet.enums.OrderStatus;
import com.ball.biz.bet.order.settle.analyze.bo.AnalyzeResult;
import com.ball.biz.order.bo.OrderFinishBo;
import com.ball.biz.order.entity.OrderInfo;
import com.ball.common.service.IBaseService;
import com.baomidou.mybatisplus.extension.service.IService;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

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

    void updateStatus(String orderId, OrderStatus pre, OrderStatus next, String reason);

    /**
     * 仅仅结算投注选项
     */
    void settled(String orderId, AnalyzeResult analyzeResult);

    void finish(OrderFinishBo bo);

    /**
     * 只要未完成都可以取消
     */
    void cancel(String orderId, String reason);

    /**
     * 投注失败
     */
    void betFail(String orderId, String reason);

    /**
     * 统计，用户单场投注金额
     */
    BigDecimal statBetAmount(Long userId, String matchId);

    PageResult<OrderInfo> queryByStatusAndDate(List<String> statusList, LocalDate start, LocalDate end, Integer pageIndex, Integer pageSize);

    /**
     * 查询用户已完成订单，参数都必传
     */
    List<OrderInfo> queryUserFinish(LocalDate start, LocalDate end, Long proxy1, Long proxy2, Long proxy3, Long userId);
}
