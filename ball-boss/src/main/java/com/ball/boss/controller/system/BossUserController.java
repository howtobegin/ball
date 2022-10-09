package com.ball.boss.controller.system;


import com.ball.base.model.PageResult;
import com.ball.base.util.BeanUtil;
import com.ball.boss.context.BossUserContext;
import com.ball.boss.controller.system.vo.SingleParamVo;
import com.ball.boss.controller.system.vo.request.*;
import com.ball.boss.controller.system.vo.response.*;
import com.ball.boss.dao.entity.BossRole;
import com.ball.boss.dao.entity.BossUserInfo;
import com.ball.boss.service.system.AuthorizeService;
import com.ball.boss.service.system.UserService;
import com.ball.boss.service.system.model.RoleMenuInfo;
import com.ball.common.service.GoogleAuthService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


@Api(tags = "用户相关接口")
@RestController
@RequestMapping("/boss/system/user")
public class BossUserController {
    @Autowired
    private UserService userService;

    @Autowired
    private AuthorizeService authorizeService;

    @Autowired
    private GoogleAuthService googleAuthService;

    @ApiOperation(value = "获取用户角色和菜单信息")
    @RequestMapping(value = "getRoleMenu", method = RequestMethod.GET)
    public UserRoleMenuVo getUserRoleMenu() {
        RoleMenuInfo roleMenuInfo = userService.getUserRoleMenuInfo(BossUserContext.getUserId());
        //组装vo
        return UserRoleMenuVo.builder()
                .menus(roleMenuInfo.getMenus().stream().map(menu -> BeanUtil.copy(menu, MenuResponseVo.class)).collect(Collectors.toList()))
                .roles(roleMenuInfo.getRoles().stream().map(role -> BeanUtil.copy(role, RoleResponseVo.class)).collect(Collectors.toList()))
                .build();
    }

    @ApiOperation("获取对应用户的角色信息")
    @ApiImplicitParam(value = "用户编号", name = "userId", required = true)
    @RequestMapping(value = "getRole", method =  RequestMethod.GET)
    public List<RoleResponseVo> getUserRole(@RequestParam String userId) {
        List<BossRole> roles = userService.getUserRoles(userId);
        if (CollectionUtils.isEmpty(roles)) {
            return new ArrayList<>();
        }
        return roles.stream().map(role -> BeanUtil.copy(role, RoleResponseVo.class)).collect(Collectors.toList());
    }

    @ApiOperation(value = "授权用户角色")
    @RequestMapping(value = "authorizeRole", method = RequestMethod.POST)
    public void authorizeRole(@RequestBody @Valid UserRoleRequestVo userRole) {
        authorizeService.updateUserAuthorize(userRole.getRoleIds(), userRole.getUserId());
    }

    @ApiOperation(value = "添加用户")
    @RequestMapping(value = "add", method = RequestMethod.POST)
    public String add(@RequestBody @Valid UserAddVo user) {
        return userService.insert(BeanUtil.copy(user, BossUserInfo.class));
    }

    @ApiOperation(value = "修改用户")
    @RequestMapping(value = "update", method = RequestMethod.POST)
    public void update(@RequestBody @Valid UserUpdateVo user) {
        userService.update(BeanUtil.copy(user, BossUserInfo.class));
    }

    @ApiOperation("删除用户")
    @RequestMapping(value = "delete", method =  RequestMethod.POST)
    public void delete(@RequestBody @Valid SingleParamVo<String> param) {
        userService.delete(param.getData());
    }

    @ApiOperation(value = "修改用户密码")
    @RequestMapping(value = "changePassword", method = RequestMethod.POST)
    public void changePassword(@RequestBody @Valid UserPasswordRequestVo request) {
        Assert.isTrue(request.isNotSame(), "新密码不能和原密码一致");
        userService.changePassword(request.getOldPassword(), request.getNewPassword());
    }

    @ApiOperation("修改用户锁定状态")
    @RequestMapping(value = "changeLock", method =  RequestMethod.POST)
    public void changeLock(@RequestBody @Valid UserLockRequestVo userLockRequest) {
        userService.changeLock(userLockRequest.getUserId(), userLockRequest.getLockFlag());
    }

    @ApiOperation("查询用户信息")
    @ApiImplicitParam(value = "用户编号", name = "userId", required = true)
    @RequestMapping(value = "queryById", method =  RequestMethod.GET)
    public UserInfoVo queryById(@RequestParam String userId) {
        BossUserInfo userInfo = userService.queryById(userId);
        return BeanUtil.copy(userInfo, UserInfoVo.class);
    }


    @ApiOperation("分页查询用户信息")
    @RequestMapping(value = "queryPaging", method =  RequestMethod.POST)
    public PageResult<UserInfoVo> queryPaging(@RequestBody @Valid UserQueryRequestVo request) {
        PageResult<BossUserInfo> queryResult = userService.queryPaging(BeanUtil.copy(request, BossUserInfo.class));
        PageResult<UserInfoVo> returnValue = new PageResult<>(null, queryResult.getTotalNum(), request.getPageIndex(), request.getPageSize());
        if (CollectionUtils.isEmpty(queryResult.getRows())) {
            returnValue.setRows(new ArrayList<>());
        } else {
            returnValue.setRows(queryResult.getRows().stream().map(o -> BeanUtil.copy(o, UserInfoVo.class)).collect(Collectors.toList()));
        }
        return returnValue;
    }

    @ApiOperation("获取谷歌验证信息")
    @GetMapping("getGoogleSecret")
    public GoogleSecretResp getGoogleSecret() {
        return BeanUtil.copy(googleAuthService.getGoogleAuth(BossUserContext.get().getAccount()), GoogleSecretResp.class);
    }

    @ApiOperation("绑定谷歌验证码")
    @RequestMapping(value = "bindGoogle", method = {RequestMethod.POST})
    public void bindGoogle(@RequestBody @Valid GoogleCodeReq googleCode) {
        userService.bindGoogle(BossUserContext.get().getAccount(), googleCode.getCode());
    }


    @ApiOperation("解绑谷歌验证码")
    @RequestMapping(value = "unbindGoogle", method = {RequestMethod.POST})
    public void unbindGoogle(@RequestBody @Valid GoogleCodeReq googleCode) {
        userService.unbindGoogle(BossUserContext.get().getAccount(), googleCode.getCode());
    }
}
