package com.ball.app.controller.match;

import com.ball.app.controller.match.vo.HandicapMatchOddsResp;
import com.ball.app.controller.match.vo.OutrightsGroupResp;
import com.ball.app.controller.match.vo.OutrightsWinnerResp;
import com.ball.app.service.BizOddsService;
import com.ball.app.service.BizOutrightsService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 *
 * @author lhl
 * @date 2022/10/20 上午10:22
 */
@Slf4j
@Api(tags = "世界杯")
@RestController
@RequestMapping("/app/outrights/odds")
public class OutrightsController {
    @Autowired
    private BizOddsService bizOddsService;
    @Autowired
    private BizOutrightsService bizOutrightsService;

    @ApiOperation("赛事 - 主要玩法和波胆")
    @PostMapping("list")
    public List<HandicapMatchOddsResp> list() {
        return bizOddsService.mainOddsList(bizOutrightsService.getSchedules(), null);
    }

    @ApiOperation("小组玩法")
    @PostMapping("group/list")
    public List<OutrightsGroupResp> groupList() {
        return bizOutrightsService.groupList();
    }

    @ApiOperation("冠军")
    @PostMapping("winner/list")
    public List<OutrightsWinnerResp> winnerList() {
        return bizOutrightsService.winnerList();
    }
}
