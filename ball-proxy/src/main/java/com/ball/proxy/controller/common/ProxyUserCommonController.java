package com.ball.proxy.controller.common;

import com.ball.base.context.UserContext;
import com.ball.base.model.Const;
import com.ball.biz.user.bo.UserStatusBO;
import com.ball.biz.user.mapper.ext.UserExtMapper;
import com.ball.proxy.controller.common.vo.*;
import com.ball.proxy.service.UserOperationService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

import static com.ball.proxy.controller.assist.ProxyAssist.getLike;

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

    @Autowired
    private UserExtMapper userExtMapper;

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

    @ApiOperation("修改密码")
    @PostMapping("updatePassword")
    public void updatePassword(@RequestBody @Valid UpdatePasswordReq req) {
        userOperationService.updatePassword(req.getUserNo(), req.getPassword());
    }

    @ApiOperation("修改用户信息")
    @PostMapping("updateUserInfo")
    public void updateUserInfo(@RequestBody @Valid UpdateProxyInfo req) {
        if (req.invalid()) {
            return;
        }
        userOperationService.updateUserInfo(req);
    }

    @ApiOperation("查询下线状态")
    @PostMapping("queryByType")
    public UserStatusResp queryByType(@RequestBody @Valid UserTypeReq req) {
        // 得到前缀
        String proxyInfo = getLike(UserContext.getProxyInfo(), UserContext.getUserNo().toString());
        UserStatusBO bo = userExtMapper.selectStatusByType(req.getUserType(), proxyInfo);
        if (bo == null) {
            return new UserStatusResp();
        } else {
            return new UserStatusResp()
                    .setLockCount(bo.getLockCount())
                    .setNormalCount(bo.getNormalCount());
        }
    }


}
