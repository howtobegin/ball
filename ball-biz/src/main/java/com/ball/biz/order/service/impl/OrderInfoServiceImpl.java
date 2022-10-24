package com.ball.biz.order.service.impl;

import com.ball.base.transaction.TransactionSupport;
import com.ball.base.util.BizAssert;
import com.ball.biz.bet.enums.OrderStatus;
import com.ball.biz.exception.BizErrCode;
import com.ball.biz.order.entity.OrderInfo;
import com.ball.biz.order.mapper.OrderInfoMapper;
import com.ball.biz.order.service.IOrderInfoService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 订单表 服务实现类
 * </p>
 *
 * @author lhl
 * @since 2022-10-20
 */
@Service
public class OrderInfoServiceImpl extends ServiceImpl<OrderInfoMapper, OrderInfo> implements IOrderInfoService {
    @Autowired
    private TransactionSupport transactionSupport;

    @Override
    public OrderInfo queryByOrderId(String orderId) {
        return lambdaQuery().eq(OrderInfo::getOrderId, orderId).one();
    }

    @Override
    public void updateStatus(String orderId, OrderStatus pre, OrderStatus next) {
        transactionSupport.execute(()->{
            boolean update = lambdaUpdate().eq(OrderInfo::getOrderId, orderId)
                    .eq(OrderInfo::getStatus, pre.getCode())

                    .set(OrderInfo::getStatus, next.getCode())
                    .update();
            BizAssert.isTrue(update, BizErrCode.UPDATE_FAIL);
            // TODO 增加历史
        });
    }
}
