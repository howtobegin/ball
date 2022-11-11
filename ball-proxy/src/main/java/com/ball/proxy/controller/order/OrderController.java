package com.ball.proxy.controller.order;

import com.ball.proxy.controller.order.vo.OrderHistoryResp;
import com.ball.proxy.controller.order.vo.OrderReq;
import com.ball.proxy.controller.order.vo.OrderResp;
import com.ball.proxy.service.BizOrderQueryService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

/**
 * @author lhl
 * @date 2022/10/20 下午3:46
 */
@Api(tags = "订单信息")
@RestController
@RequestMapping("/proxy/order")
public class OrderController {
    @Autowired
    private BizOrderQueryService bizOrderQueryService;

    @ApiOperation("交易状况")
    @PostMapping("bet/current")
    public List<OrderResp> betCurrent(@RequestBody @Valid OrderReq req) {
        return bizOrderQueryService.current(req.getUserId());
    }

    @ApiOperation("账户历史")
    @PostMapping("bet/history")
    public List<OrderHistoryResp> betHistory(@RequestBody @Valid OrderReq req) {
        return bizOrderQueryService.history(req.getUserId());
    }
}
