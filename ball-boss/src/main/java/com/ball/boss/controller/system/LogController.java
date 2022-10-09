package com.ball.boss.controller.system;


import com.ball.base.util.BeanUtil;
import com.ball.boss.controller.system.vo.CheckLogRequestVo;
import com.ball.boss.controller.system.vo.CheckLogVo;
import com.ball.boss.dao.entity.BossCheckLog;
import com.ball.boss.service.system.LogService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Api(tags = "日志相关接口")
@RestController
@RequestMapping("/boss/common/log")
public class LogController {

    @Autowired
    private LogService logService;

    @ApiOperation(value = "查询审核日志信息")
    @RequestMapping(value = "queryCheckLog", method = RequestMethod.POST)
    public List<CheckLogVo> queryCheckLog(@RequestBody @Valid CheckLogRequestVo request) {
        List<BossCheckLog> checkLogPos = logService.queryCheckLogByBizId(request.getBizId(), request.getBizCode());
        if (CollectionUtils.isEmpty(checkLogPos)) {
            return new ArrayList<>();
        }
        return checkLogPos.stream().map(o -> BeanUtil.copy(o, CheckLogVo.class)).collect(Collectors.toList());
    }
}
