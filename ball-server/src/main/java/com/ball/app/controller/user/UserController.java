package com.ball.app.controller.user;

import com.ball.app.controller.user.vo.ChangePasswordReq;
import com.ball.app.controller.user.vo.FirstChangePasswordReq;
import com.ball.app.controller.user.vo.LoginAccountReq;
import com.ball.app.controller.user.vo.UserInfoResp;
import com.ball.app.interceptor.LoginInterceptor;
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

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

/**
 * @author JimChery
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

    @ApiOperation("首次修改密码")
    @PostMapping("changePasswordFirst")
    public void changePasswordFirst(@RequestBody @Valid FirstChangePasswordReq req) {
        // 校验密码合法性
        BizAssert.isTrue(PasswordUtil.checkValid(req.getPassword()), BizErrCode.USER_PASSWORD_INVALID);
        userInfoService.firstChangePassword(UserContext.getUserNo(), req.getPassword());
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

    @ApiOperation("登出接口")
    @RequestMapping(value = "logout", method = {RequestMethod.GET, RequestMethod.POST})
    public void logout(HttpServletRequest request) {
        HttpSession session = request.getSession();
        String userNo = (String) session.getAttribute(LoginInterceptor.SESSION_USER);
        session.removeAttribute(LoginInterceptor.SESSION_USER);
        if (userNo != null) {
            userInfoService.logout(Long.valueOf(userNo));
        }
        session.invalidate();
    }
}
