package com.ball.app.controller.match;

import com.ball.app.controller.match.vo.HandicapMatchOddsResp;
import com.ball.app.controller.match.vo.OddsReq;
import com.ball.app.service.BizOddsService;
import com.ball.biz.match.service.IOddsScoreService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

/**
 *
 * @author lhl
 * @date 2022/10/20 上午10:22
 */
@Api(tags = "指数信息")
@RestController
@RequestMapping("/app/odds")
public class OddsController {
    @Autowired
    private BizOddsService bizOddsService;
    @Autowired
    private IOddsScoreService oddsScoreService;

    @ApiOperation("主页滚球或主要玩法")
    @PostMapping("list")
    public List<HandicapMatchOddsResp> oddsList(@RequestBody @Valid OddsReq req) {
        if (req.getType() == 1) {
            return bizOddsService.handicapOddsList(req.getMatchId());
        }
        return bizOddsService.oddsList(req.getType(), req.getLeagueIds(), req.getMatchId());
    }
}
