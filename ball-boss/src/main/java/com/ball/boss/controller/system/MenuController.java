package com.ball.boss.controller.system;

import com.ball.base.util.BeanUtil;
import com.ball.boss.controller.system.vo.MenuBaseVo;
import com.ball.boss.controller.system.vo.request.MenuUpdateVo;
import com.ball.boss.controller.system.vo.response.MenuResponseVo;
import com.ball.boss.dao.entity.BossMenu;
import com.ball.boss.service.system.MenuService;
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

@Api(tags = "菜单管理接口")
@RestController
@RequestMapping("/boss/system/menu")
public class MenuController {

    @Autowired
    private MenuService menuService;

    @ApiOperation("新增菜单")
    @RequestMapping(value = "add", method = RequestMethod.POST)
    public String add(@RequestBody @Valid MenuBaseVo menu) {
        return menuService.insert(BeanUtil.copy(menu, BossMenu.class));
    }

    @ApiOperation("修改菜单")
    @RequestMapping(value = "update", method = RequestMethod.POST)
    public void update(@RequestBody @Valid MenuUpdateVo menu) {
        menuService.update(BeanUtil.copy(menu, BossMenu.class));
    }

    @ApiOperation("删除菜单")
    @ApiImplicitParam(value = "菜单编号", name = "menuId", required = true)
    @RequestMapping(value = "delete", method = {RequestMethod.POST, RequestMethod.GET})
    public void delete(@RequestParam String menuId) {
        menuService.delete(menuId);
    }

    @ApiOperation("查询所有菜单")
    @RequestMapping(value = "queryAll", method = RequestMethod.GET)
    public List<MenuResponseVo> queryAll() {
        List<BossMenu> menus = menuService.queryAll();
        if (CollectionUtils.isEmpty(menus)) {
            return new ArrayList<>();
        }
        return menus.stream().map(o -> BeanUtil.copy(o, MenuResponseVo.class)).collect(Collectors.toList());
    }
}
