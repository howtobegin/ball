package com.ball.app.service;

import com.ball.app.controller.order.vo.BetHistoryReq;
import com.ball.app.controller.order.vo.OrderResp;
import com.ball.base.context.UserContext;
import com.ball.base.model.PageResult;
import com.ball.base.util.BeanUtil;
import com.ball.biz.bet.enums.OrderStatus;
import com.ball.biz.order.entity.OrderInfo;
import com.ball.biz.order.service.IOrderInfoService;
import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author lhl
 * @date 2022/10/25 下午5:35
 */
@Slf4j
@Component
public class BizOrderQueryService {
    @Autowired
    private IOrderInfoService orderInfoService;

    public PageResult<OrderResp> history(BetHistoryReq req) {
        Integer pageIndex = req.getPageIndex();
        Integer pageSize = req.getPageSize();
        LocalDate start = req.getStart();
        LocalDate end = req.getEnd();
        List<String> statusList = OrderStatus.finishCodes(req.getType() != 1);

        Page<OrderInfo> page = new Page<>(pageIndex, pageIndex);
        LambdaQueryChainWrapper<OrderInfo> wrapper = orderInfoService.lambdaQuery()
                .eq(OrderInfo::getUserId, UserContext.getUserNo())
                .in(OrderInfo::getStatus, statusList)
                .gt(start != null, OrderInfo::getCreateTime, start)
                .lt(end != null, OrderInfo::getCreateTime, end);
        wrapper.page(page);
        List<OrderInfo> records = page.getRecords();
        List<OrderResp> respList = records.stream().map(o -> BeanUtil.copy(o, OrderResp.class)).collect(Collectors.toList());
        return new PageResult<>(respList, page.getTotal(), pageIndex, pageSize);
    }
}
