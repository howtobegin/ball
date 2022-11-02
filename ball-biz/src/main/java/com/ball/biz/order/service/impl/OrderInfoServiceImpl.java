package com.ball.biz.order.service.impl;

import com.alibaba.fastjson.JSON;
import com.ball.base.model.PageResult;
import com.ball.base.model.enums.YesOrNo;
import com.ball.base.transaction.TransactionSupport;
import com.ball.base.util.BizAssert;
import com.ball.biz.account.enums.AccountTransactionType;
import com.ball.biz.account.service.IUserAccountService;
import com.ball.biz.bet.enums.OrderStatus;
import com.ball.biz.bet.order.settle.analyze.bo.AnalyzeResult;
import com.ball.biz.exception.BizErrCode;
import com.ball.biz.order.bo.OrderFinishBo;
import com.ball.biz.order.entity.OrderInfo;
import com.ball.biz.order.mapper.OrderInfoMapper;
import com.ball.biz.order.service.IOrderHistoryService;
import com.ball.biz.order.service.IOrderInfoService;
import com.ball.biz.order.service.IOrderStatService;
import com.ball.biz.order.service.IOrderSummaryService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

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
    @Autowired
    private IOrderHistoryService orderHistoryService;
    @Autowired
    private IUserAccountService userAccountService;
    @Autowired
    private IOrderStatService orderStatService;
    @Autowired
    private IOrderSummaryService orderSummaryService;

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
                    .set(next.isFinish(), OrderInfo::getFinishTime, LocalDateTime.now())
                    .update();
            BizAssert.isTrue(update, BizErrCode.UPDATE_FAIL);
            // 增加历史
            orderHistoryService.saveLatest(queryByOrderId(orderId));
        });
    }

    @Override
    public void updateStatus(String orderId, OrderStatus pre, OrderStatus next, String reason) {
        log.info("orderId {} pre {} next {}",orderId, pre, next);
        BizAssert.isTrue(!pre.isFinish(), BizErrCode.UPDATE_FAIL);
        transactionSupport.execute(()->{
            boolean update = lambdaUpdate().eq(OrderInfo::getOrderId, orderId)
                    .eq(OrderInfo::getStatus, pre.getCode())

                    .set(OrderInfo::getStatus, next.getCode())
                    .set(next.isFinish(), OrderInfo::getFinishTime, LocalDateTime.now())
                    .set(!StringUtils.isEmpty(reason), OrderInfo::getReason, reason)
                    .update();
            BizAssert.isTrue(update, BizErrCode.UPDATE_FAIL);
            // 增加历史
            orderHistoryService.saveLatest(queryByOrderId(orderId));
        });
    }

    @Override
    public void settled(String orderId, AnalyzeResult analyzeResult) {
        log.info("orderId {} analyzeResult {}", orderId, JSON.toJSONString(analyzeResult));
        OrderInfo order = queryByOrderId(orderId);
        if (OrderStatus.SETTLED.isMe(order.getStatus())) {
            return;
        }
        transactionSupport.execute(()->{
            boolean update = lambdaUpdate()
                    .set(OrderInfo::getBetResult, analyzeResult.getBetResult())
                    .set(analyzeResult.getHomeScore() != null, OrderInfo::getHomeLastScore, analyzeResult.getHomeScore())
                    .set(analyzeResult.getAwayScore() != null, OrderInfo::getAwayLastScore, analyzeResult.getAwayScore())
                    .set(OrderInfo::getStatus, OrderStatus.SETTLED.getCode())
                    .set(OrderInfo::getSettleStatus, YesOrNo.YES.v)


                    .eq(OrderInfo::getId, order.getId())
                    .eq(OrderInfo::getStatus, OrderStatus.CONFIRM.getCode())
                    .update();
            log.info("orderId {} update {}", order, update);
            BizAssert.isTrue(update, BizErrCode.UPDATE_FAIL);
            // 增加历史
            orderHistoryService.saveLatest(queryByOrderId(orderId));
        });
    }

    @Override
    public void finish(OrderFinishBo bo) {
        log.info("orderId {}", bo.getOrderId());

        transactionSupport.execute(()->{
            boolean update = lambdaUpdate()
                    .set(OrderInfo::getStatus, OrderStatus.FINISH.getCode())
                    .set(OrderInfo::getResultAmount, bo.getResultAmount())
                    .set(OrderInfo::getValidAmount, bo.getValidAmount())
                    .set(OrderInfo::getProxy1Amount, bo.getProxy1Amount())
                    .set(OrderInfo::getProxy2Amount, bo.getProxy2Amount())
                    .set(OrderInfo::getProxy3Amount, bo.getProxy3Amount())
                    .set(OrderInfo::getBackwaterAmount, bo.getBackwaterAmount())
                    .set(OrderInfo::getFinishTime, LocalDateTime.now())

                    .eq(OrderInfo::getOrderId, bo.getOrderId())
                    .eq(OrderInfo::getStatus, bo.getPre().getCode())
                    .update();
            log.info("orderId {} update {}", update);
            BizAssert.isTrue(update, BizErrCode.UPDATE_FAIL);

            // 处理订单统计
            OrderInfo order = queryByOrderId(bo.getOrderId());
            orderStatService.newOrderFinish(order);
            orderSummaryService.newOrderFinish(order);
        });
    }

    @Override
    public void cancel(String orderId, String reason) {
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

        transactionSupport.execute(()->{
            userAccountService.unfreeze(orderId, AccountTransactionType.TRADE);
            updateStatus(orderId, status, OrderStatus.CANCEL, reason);
        });
    }

    @Override
    public BigDecimal statBetAmount(Long userId, String matchId) {
        if (StringUtils.isEmpty(matchId) || userId == null) {
            return BigDecimal.ZERO;
        }
        QueryWrapper<OrderInfo> query = new QueryWrapper<>();

        query.select("sum(bet_amount) bet_amount")
                .eq("user_id", userId)
                .eq("match_id", matchId);
        OrderInfo order = lambdaQuery().getBaseMapper().selectOne(query);
        return Optional.ofNullable(order).map(OrderInfo::getBetAmount).orElse(BigDecimal.ZERO);
    }

    @Override
    public PageResult<OrderInfo> queryByStatusAndDate(List<String> statusList, LocalDate start, LocalDate end, Integer pageIndex, Integer pageSize) {
        Page<OrderInfo> page = new Page<>(pageIndex, pageSize);
        LambdaQueryChainWrapper<OrderInfo> wrapper = lambdaQuery()
                .in(OrderInfo::getStatus, statusList)
                .gt(start != null, OrderInfo::getCreateTime, start)
                .lt(end != null, OrderInfo::getCreateTime, end);
        wrapper.page(page);
        List<OrderInfo> records = page.getRecords();
        return new PageResult<>(records, page.getTotal(), pageIndex, pageSize);
    }
}
