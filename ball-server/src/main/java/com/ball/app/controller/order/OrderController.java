package com.ball.app.controller.order;

import com.ball.app.controller.order.vo.*;
import com.ball.app.service.BizBetService;
import com.ball.app.service.BizOrderQueryService;
import com.ball.base.context.UserContext;
import com.ball.base.util.BeanUtil;
import com.ball.biz.bet.order.bo.BetBo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.List;

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
    @Autowired
    private BizBetService bizBetService;

    @ApiOperation("下注")
    @PostMapping("bet")
    public void bet(@RequestBody @Valid BetReq req) {
        BetBo bo = BeanUtil.copy(req, BetBo.class);
        bo.setUserNo(UserContext.getUserNo());
        bo.setBetTime(LocalDateTime.now());
        bizBetService.bet(bo);
    }

    @ApiOperation("交易状况")
    @PostMapping("bet/current")
    public List<OrderResp> betCurrent() {
        return bizOrderQueryService.current();
    }

    @ApiOperation("账户历史")
    @PostMapping("bet/history")
    public List<OrderHistoryResp> betHistory(@RequestBody @Valid BetHistoryReq req) {
        return bizOrderQueryService.history(UserContext.getUserNo(), req.getStart(), req.getEnd());
    }

    @ApiOperation("账户历史 - 指定某天")
    @PostMapping("bet/history/date")
    public List<OrderResp> betHistoryDate(@RequestBody @Valid BetHistoryDateReq req) {
        return bizOrderQueryService.historyDate(UserContext.getUserNo(), req.getDate());
    }
}
