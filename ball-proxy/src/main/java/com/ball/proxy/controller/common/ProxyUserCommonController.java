package com.ball.proxy.controller.common;

import com.ball.proxy.controller.common.vo.UserNoReq;
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
 * @since 2022-11-02 14:56
 */
@Api(tags = "代理及会员管理")
@RestController
@RequestMapping("/proxy/user/common/")
public class ProxyUserCommonController {

    @Autowired
    private UserOperationService userOperationService;

    @ApiOperation("停用会员或代理")
    @PostMapping("lock")
    public void lock(@RequestBody @Valid UserNoReq req) {
        userOperationService.lock(req.getUserNo());
    }

    @ApiOperation("启用会员或代理")
    @PostMapping("unlock")
    public void unlock(@RequestBody @Valid UserNoReq req) {
        userOperationService.unlock(req.getUserNo());
    }
}
