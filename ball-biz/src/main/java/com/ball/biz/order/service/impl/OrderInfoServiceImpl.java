package com.ball.biz.order.service.impl;

import com.ball.base.transaction.TransactionSupport;
import com.ball.base.util.BizAssert;
import com.ball.biz.bet.enums.OrderStatus;
import com.ball.biz.bet.order.settle.analyze.bo.AnalyzeResult;
import com.ball.biz.exception.BizErrCode;
import com.ball.biz.order.entity.OrderInfo;
import com.ball.biz.order.mapper.OrderInfoMapper;
import com.ball.biz.order.service.IOrderInfoService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
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
@Slf4j
@Service
public class OrderInfoServiceImpl extends ServiceImpl<OrderInfoMapper, OrderInfo> implements IOrderInfoService {
    @Autowired
    private TransactionSupport transactionSupport;

    @Override
    public OrderInfo queryByOrderId(String orderId) {
        OrderInfo order = lambdaQuery().eq(OrderInfo::getOrderId, orderId).one();
        BizAssert.notNull(order, BizErrCode.DATA_NOT_EXISTS);
        return order;
    }

    @Override
    public void updateStatus(String orderId, OrderStatus pre, OrderStatus next) {
        log.info("orderId {} pre {} next {}",orderId, pre, next);
        BizAssert.isTrue(!pre.isFinish(), BizErrCode.UPDATE_FAIL);
        transactionSupport.execute(()->{
            boolean update = lambdaUpdate().eq(OrderInfo::getOrderId, orderId)
                    .eq(OrderInfo::getStatus, pre.getCode())

                    .set(OrderInfo::getStatus, next.getCode())
                    .update();
            BizAssert.isTrue(update, BizErrCode.UPDATE_FAIL);
            // TODO 增加历史
        });
    }

    @Override
    public void settled(String orderId, AnalyzeResult analyzeResult) {
        log.info("orderId {}", orderId);
        OrderInfo order = queryByOrderId(orderId);
        if (OrderStatus.SETTLED.isMe(order.getStatus())) {
            return;
        }
        transactionSupport.execute(()->{
            lambdaUpdate()
                    .set(OrderInfo::getBetResult , analyzeResult.getBetResult())
                    .set(analyzeResult.getHomeScore() != null, OrderInfo::getHomeLastScore, analyzeResult.getHomeScore())
                    .set(analyzeResult.getAwayScore() != null, OrderInfo::getAwayLastScore, analyzeResult.getAwayScore())
                    .set(OrderInfo::getStatus,  OrderStatus.SETTLED.getCode())

                    .eq(OrderInfo::getId, order.getId())
                    .eq(OrderInfo::getStatus, OrderStatus.CONFIRM.getCode())
                    .update();
        });
    }

    @Override
    public void award(String orderId) {
        log.info("orderId {}", orderId);
        // TODO 账务变动
    }

    @Override
    public void cancel(String orderId) {
        log.info("orderId {}", orderId);
        OrderInfo order = queryByOrderId(orderId);
        boolean isCancel = OrderStatus.cancelCodes().contains(order.getStatus());
        if (isCancel) {
            log.info("isCancel {}", isCancel);
            return;
        }
        OrderStatus status = OrderStatus.parse(order.getStatus());
        log.info("status {}", status);
        BizAssert.notNull(status, BizErrCode.PARAM_ERROR_DESC,"status");

        updateStatus(orderId, status, OrderStatus.CANCEL);
    }
}
