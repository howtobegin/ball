package com.ball.proxy.controller.user;

import com.ball.proxy.controller.user.vo.AddUserReq;
import com.ball.proxy.controller.user.vo.UserRefundReq;
import com.ball.proxy.service.UserOperationService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

/**
 * @author JimChery
 */
@Api(tags = "代理会员管理")
@RestController
@RequestMapping("/proxy/user/")
public class UserController {

    @Autowired
    private UserOperationService userOperationService;

    @ApiOperation("添加会员")
    @PostMapping("add")
    public void add(@RequestBody @Valid AddUserReq req) {
        userOperationService.addUser(req);
    }

    @ApiOperation("添加退水和限额")
    @PostMapping("addRefund")
    public void addRefund(@RequestBody @Valid UserRefundReq req) {
        userOperationService.addRefund(req);
    }
}
