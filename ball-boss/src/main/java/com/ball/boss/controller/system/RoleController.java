package com.ball.boss.controller.system;

import com.ball.base.model.PageResult;
import com.ball.base.util.BeanUtil;
import com.ball.boss.controller.system.vo.RoleBaseVo;
import com.ball.boss.controller.system.vo.request.RoleLockRequestVo;
import com.ball.boss.controller.system.vo.request.RoleMenuRequestVo;
import com.ball.boss.controller.system.vo.request.RoleQueryRequestVo;
import com.ball.boss.controller.system.vo.request.RoleUpdateVo;
import com.ball.boss.controller.system.vo.response.MenuResponseVo;
import com.ball.boss.controller.system.vo.response.RoleMenuResponseVo;
import com.ball.boss.controller.system.vo.response.RoleResponseVo;
import com.ball.boss.dao.entity.BossRole;
import com.ball.boss.service.system.AuthorizeService;
import com.ball.boss.service.system.RoleService;
import com.ball.boss.service.system.model.RoleMenuInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Api(tags = "角色管理接口")
@RestController
@RequestMapping("/boss/system/role")
public class RoleController {
    @Autowired
    private RoleService roleService;
    @Autowired
    private AuthorizeService authorizeService;

    @ApiOperation("新增角色")
    @RequestMapping(value = "add", method = RequestMethod.POST)
    public String add(@RequestBody @Valid RoleBaseVo role) {
        return roleService.insert(BeanUtil.copy(role, BossRole.class));
    }

    @ApiOperation("修改角色")
    @RequestMapping(value = "update", method = RequestMethod.POST)
    public void update(@RequestBody @Valid RoleUpdateVo role) {
        roleService.update(BeanUtil.copy(role, BossRole.class));
    }

    @ApiOperation("删除角色")
    @ApiImplicitParam(value = "角色编号", name = "roleId", required = true)
    @RequestMapping(value = "delete", method = {RequestMethod.POST, RequestMethod.GET})
    public void delete(@RequestParam String roleId) {
        roleService.delete(roleId);
    }

    @ApiOperation("锁定或解锁角色")
    @RequestMapping(value = "changeLock", method = RequestMethod.POST)
    public void changeLock(@RequestBody @Valid RoleLockRequestVo roleLock) {
        roleService.lockOrNot(roleLock.getRoleId(), roleLock.getLockFlag());
    }

    @ApiOperation("查询所有角色")
    @RequestMapping(value = "queryAll", method = RequestMethod.GET)
    public List<RoleResponseVo> queryRoles() {
        List<BossRole> rolePos = roleService.queryAll();
        if (CollectionUtils.isEmpty(rolePos)) {
            return new ArrayList<>();
        }
        return rolePos.stream().map(r -> BeanUtil.copy(r, RoleResponseVo.class)).collect(Collectors.toList());
    }

    @ApiOperation("分页查询角色信息")
    @RequestMapping(value = "queryPaging", method = RequestMethod.POST)
    public PageResult<RoleResponseVo> queryPaging(@RequestBody @Valid RoleQueryRequestVo request) {
        PageResult<BossRole> queryResult = roleService.queryPaging(BeanUtil.copy(request, BossRole.class));
        PageResult<RoleResponseVo> returnValue = new PageResult<>(null, queryResult.getTotalNum(), request.getPageIndex(), request.getPageSize());
        if (CollectionUtils.isEmpty(queryResult.getRows())) {
            returnValue.setRows(new ArrayList<>());
        } else {
            returnValue.setRows(queryResult.getRows().stream().map(o -> BeanUtil.copy(o, RoleResponseVo.class)).collect(Collectors.toList()));
        }
        return returnValue;
    }

    @ApiOperation("查询单个角色")
    @ApiImplicitParam(value = "角色编号", name = "roleId", required = true)
    @RequestMapping(value = "queryById", method =  RequestMethod.GET)
    public RoleResponseVo queryById(@RequestParam String roleId) {
        return BeanUtil.copy(roleService.queryById(roleId), RoleResponseVo.class);
    }

    @ApiOperation("授权角色菜单")
    @RequestMapping(value = "authorizeMenu", method = RequestMethod.POST)
    public void authorizeMenu(@RequestBody @Valid RoleMenuRequestVo roleMenu) {
        authorizeService.updateRoleAuthorize(roleMenu.getMenuIds(), roleMenu.getRoleId());
    }

    @ApiOperation("查询角色以及对应的菜单信息")
    @ApiImplicitParam(value = "角色编号", name = "roleId", required = true)
    @RequestMapping(value = "queryDetail", method =  RequestMethod.GET)
    public RoleMenuResponseVo queryDetail(@RequestParam String roleId) {
        RoleMenuInfo roleMenuInfo = roleService.queryDetail(roleId);
        BossRole role = roleMenuInfo.getRoles().get(0);
        RoleMenuResponseVo responseVo = BeanUtil.copy(role, RoleMenuResponseVo.class);
        if (CollectionUtils.isEmpty(roleMenuInfo.getMenus())) {
            responseVo.setMenus(new ArrayList<>());
        } else {
            responseVo.setMenus(roleMenuInfo.getMenus().stream().map(m -> BeanUtil.copy(m, MenuResponseVo.class)).collect(Collectors.toList()));
        }
        return responseVo;
    }


}
