package com.ball.app.controller.user;

import com.ball.app.controller.user.vo.ChangePasswordReq;
import com.ball.app.controller.user.vo.LoginAccountReq;
import com.ball.app.controller.user.vo.UserInfoResp;
import com.ball.base.context.UserContext;
import com.ball.base.model.enums.YesOrNo;
import com.ball.base.util.BeanUtil;
import com.ball.base.util.BizAssert;
import com.ball.base.util.PasswordUtil;
import com.ball.biz.exception.BizErrCode;
import com.ball.biz.user.entity.UserInfo;
import com.ball.biz.user.service.IUserInfoService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * @author littlehow
 */
@Api(tags = "用户信息")
@RestController
@RequestMapping("/app/user/")
public class UserController {

    @Autowired
    private IUserInfoService userInfoService;

    @ApiOperation("获取用户信息")
    @GetMapping("get")
    public UserInfoResp get() {
        UserInfo userInfo = userInfoService.getByUid(UserContext.getUserNo());
        BizAssert.notNull(userInfo, BizErrCode.DATA_ERROR);
        UserInfoResp resp = BeanUtil.copy(userInfo, UserInfoResp.class);
        resp.setChangeAccountFlag(userInfo.getAccount().equals(userInfo.getLoginAccount()) ? YesOrNo.NO.v : YesOrNo.NO.v);
        return resp;
    }

    @ApiOperation("修改密码")
    @PostMapping("changePassword")
    public void changePassword(@RequestBody @Valid ChangePasswordReq req) {
        // 校验密码合法性
        BizAssert.isTrue(PasswordUtil.checkValid(req.getNewPassword()), BizErrCode.USER_PASSWORD_INVALID);
        userInfoService.changePassword(UserContext.getUserNo(), req.getOldPassword(), req.getNewPassword());
    }

    @ApiOperation("修改登入账号")
    @PostMapping("changeLoginAccount")
    public void changeLoginAccount(@RequestBody @Valid LoginAccountReq req) {
        req.valid();
        userInfoService.changeLogin(UserContext.getUserNo(), req.getLoginAccount());
    }

    @ApiOperation("检查登入账号是否可用")
    @PostMapping("checkLoginAccount")
    public boolean checkLoginAccount(@RequestBody @Valid LoginAccountReq req) {
        UserInfo userInfo = userInfoService.getByLogin(req.getLoginAccount());
        if (userInfo != null) {
            return false;
        }
        userInfo = userInfoService.getByAccount(req.getLoginAccount());
        return userInfo == null;
    }
}
