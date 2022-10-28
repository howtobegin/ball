package com.ball.app.controller.match;

import com.ball.app.controller.match.vo.HandicapMatchOddsResp;
import com.ball.app.controller.match.vo.LikeReq;
import com.ball.app.controller.match.vo.UnlikeReq;
import com.ball.app.service.BizOddsService;
import com.ball.base.context.UserContext;
import com.ball.biz.match.service.IFavoriteService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author lhl
 * @date 2022/10/28 上午11:42
 */
@Api(tags = "收藏夹")
@RestController
@RequestMapping("/app/user/favorite")
public class FavoriteController {
    @Autowired
    private IFavoriteService favoriteService;
    @Autowired
    private BizOddsService bizOddsService;

    @ApiOperation("收藏")
    @PostMapping("like")
    public void like(@RequestBody LikeReq req) {
        favoriteService.like(UserContext.getUserNo(), req.getType(), req.getLeagueId(), req.getMatchId());
    }

    @ApiOperation("取消收藏")
    @PostMapping("unlike")
    public void unlike(@RequestBody UnlikeReq req) {
        favoriteService.unLike(UserContext.getUserNo(), req.getMatchId());
    }

    @ApiOperation("列表")
    @RequestMapping(value = "list", method = {RequestMethod.POST, RequestMethod.GET})
    public List<HandicapMatchOddsResp> list() {
        return bizOddsService.favoriteList(UserContext.getUserNo());
    }
}
