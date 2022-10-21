package com.ball.app.controller.match;

import com.ball.app.controller.match.vo.LeagueReq;
import com.ball.app.controller.match.vo.LeagueResp;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author lhl
 * @date 2022/10/20 上午11:49
 */
@Api(tags = "联赛信息")
@RestController
@RequestMapping("/app/league")
public class LeagueController {

    @ApiOperation("联赛(<国家，联赛数据>)")
    @PostMapping("list")
    public Map<String, List<LeagueResp>> list(@RequestBody @Valid LeagueReq req) {
        return new HashMap<>();
    }
}
