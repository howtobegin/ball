package com.ball.app.controller.order;

import com.ball.app.controller.order.vo.BetHistoryReq;
import com.ball.app.controller.order.vo.BetReq;
import com.ball.app.controller.order.vo.OrderResp;
import com.ball.app.service.BizOrderQueryService;
import com.ball.base.context.UserContext;
import com.ball.base.model.PageResult;
import com.ball.base.util.BeanUtil;
import com.ball.biz.bet.order.bo.BetBo;
import com.ball.biz.bet.processor.BetProcessorHolder;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

/**
 * @author lhl
 * @date 2022/10/20 下午3:46
 */
@Api(tags = "订单信息")
@RestController
@RequestMapping("/app/order")
public class OrderController {
    @Autowired
    private BizOrderQueryService bizOrderQueryService;

    @ApiOperation("下注")
    @PostMapping("bet")
    public void bet(@RequestBody @Valid BetReq req) {
        BetBo bo = BeanUtil.copy(req, BetBo.class);
        bo.setUserNo(UserContext.getUserNo());
        BetProcessorHolder.get(req.getHandicapType()).bet(bo);
    }

    @ApiOperation("记录")
    @PostMapping("bet/history")
    public PageResult<OrderResp> betHistory(@RequestBody @Valid BetHistoryReq req) {
        return bizOrderQueryService.history(req);
    }
}
