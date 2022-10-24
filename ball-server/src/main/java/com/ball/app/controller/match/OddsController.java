package com.ball.app.controller.match;

import com.ball.app.controller.match.vo.*;
import com.google.common.collect.Lists;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
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

    @ApiOperation("主页滚球或主要玩法")
    @PostMapping("list")
    public List<HandicapMatchOddsResp> oddsList(@RequestBody @Valid OddsReq req) {
        return Lists.newArrayList();
    }

    @ApiOperation("波胆")
    @PostMapping("score/list")
    public MatchOddsScoreResp oddsScoreList(@RequestBody @Valid OddsScoreReq req) {
        return new MatchOddsScoreResp();
    }
}
