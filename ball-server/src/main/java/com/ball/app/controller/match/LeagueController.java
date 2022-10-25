package com.ball.app.controller.match;

import com.ball.app.controller.match.vo.CountryLeagueResp;
import com.ball.app.controller.match.vo.LeagueReq;
import com.ball.app.controller.match.vo.LeagueResp;
import com.ball.base.util.BeanUtil;
import com.ball.biz.match.service.ILeaguesService;
import com.google.common.collect.Lists;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author lhl
 * @date 2022/10/20 上午11:49
 */
@Api(tags = "联赛信息")
@RestController
@RequestMapping("/app/league")
public class LeagueController {
    @Autowired
    private ILeaguesService leaguesService;

    @ApiOperation("联赛(<国家，联赛数据>)")
    @PostMapping("list")
    public List<CountryLeagueResp> list(@RequestBody @Valid LeagueReq req) {
        List<CountryLeagueResp> ret = Lists.newArrayList();
        Map<String, List<LeagueResp>> map = leaguesService.list().stream()
                .map(l -> BeanUtil.copy(l, LeagueResp.class))
                .collect(Collectors.groupingBy(LeagueResp::getCountry));
        map.forEach((k, v)->{
            ret.add(CountryLeagueResp.builder()
                    .country(k)
                    .leagueResp(v)
                    .build());
        });
        return ret;
    }
}
