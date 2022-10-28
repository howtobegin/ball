package com.ball.app.controller.match;

import com.alibaba.fastjson.JSON;
import com.ball.app.controller.match.vo.HandicapMatchOddsResp;
import com.ball.app.controller.match.vo.MatchOddsResp;
import com.ball.app.controller.match.vo.MatchReq;
import com.ball.app.controller.match.vo.OddsReq;
import com.ball.app.service.BizOddsService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
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
@Slf4j
@Api(tags = "指数信息")
@RestController
@RequestMapping("/app/odds")
public class OddsController {
    @Autowired
    private BizOddsService bizOddsService;

    @ApiOperation("主页滚球或主要玩法")
    @PostMapping("list")
    public List<HandicapMatchOddsResp> oddsList(@RequestBody @Valid OddsReq req) {
        log.info("req {}", JSON.toJSONString(req));
        // 首页滚球
        if (req.getType() == 1) {
            return bizOddsService.handicapOddsList(req.getMatchId());
        }
        // 今日和早盘
        return bizOddsService.oddsList(req.getType(), req.getLeagueIds(), req.getMatchId());
    }

    @ApiOperation("指定比赛对应赔率")
    @PostMapping("match/one")
    public MatchOddsResp matchOddsList(@RequestBody @Valid MatchReq req) {
        return bizOddsService.matchOddsList(req.getMatchId());
    }
}
