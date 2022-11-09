package com.ball.proxy.controller.order.vo.stat;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * @author lhl
 * @date 2022/11/1 下午3:25
 */
@Getter
@Setter
@NoArgsConstructor
@Accessors(chain = true)
@ApiModel("注单报表-用户返回信息")
public class UserReportResp {
    @ApiModelProperty("会员ID")
    private Long userId;

    @ApiModelProperty("日期时间")
    private LocalDateTime betTime;

    @ApiModelProperty("注单号")
    private String orderId;

    @ApiModelProperty("运动类型：1 足球")
    private int sport;

    @ApiModelProperty("内容 - 联赛名")
    private String leagueName;

    @ApiModelProperty("内容 - 主队名")
    private String homeName;

    @ApiModelProperty("内容 - 客队名")
    private String awayName;

    @ApiModelProperty("内容 - 投注选项，逻辑和投注时传的一样（投注选项，选队伍的传：主队 HOME、客队 AWAY、平 DRAW；大小就传：OVER 大、UNDER 小；波胆投注：SCORE 具体比分、SCORE_OTHER 其他比分）")
    private String betOption;

    @ApiModelProperty("内容 - 赔率类型，HANDICAP[让球全场],HANDICAP_HALF[让球半场],EUROPE_ODDS[独赢], OVER_UNDER[大小球全场],OVER_UNDER_HALF[大小球半场]，CORRECT_SCORE[波胆],CORRECT_SCORE_HALF[半场波胆]")
    private String handicapType;

    @ApiModelProperty("内容 - 即时赔率，例如：0/0.5")
    private String instantHandicap;

    @ApiModelProperty("内容 - 投注时主队比分")
    private Integer homeCurrentScore;

    @ApiModelProperty("内容 - 投注时客队比分")
    private Integer awayCurrentScore;

    @ApiModelProperty("内容 - 投注赔率")
    private BigDecimal betOdds;

    @ApiModelProperty("下注金额")
    private BigDecimal betAmount;

    @ApiModelProperty("下注金额")
    private BigDecimal validAmount;

    @ApiModelProperty("结果 - 已按输赢换算")
    private BigDecimal resultAmount;

    @ApiModelProperty("赛果 - 会员投注结果(WIN 赢；LOSE 输；WIN_HALF 赢一半；LOSE_HALF 输一半；DRAW 不输不赢)")
    private String betResult;

    @ApiModelProperty("赛果 - 主队比分(已根据投注类型判定取全场还是半场比分)")
    private Integer homeScore;

    @ApiModelProperty("赛果 - 客队比分(已根据投注类型判定取全场还是半场比分)")
    private Integer awayScore;

    @ApiModelProperty("代理商占成")
    private BigDecimal proxy3Amount;

    @ApiModelProperty("总代理占成")
    private BigDecimal proxy2Amount;

    @Builder
    public UserReportResp(Long userId, LocalDateTime betTime, String orderId, int sport, String leagueName, String homeName, String awayName, String betOption, String handicapType, String instantHandicap, Integer homeCurrentScore, Integer awayCurrentScore, BigDecimal betOdds, BigDecimal betAmount, BigDecimal validAmount, BigDecimal resultAmount, String betResult, Integer homeScore, Integer awayScore, BigDecimal proxy3Amount, BigDecimal proxy2Amount) {
        this.userId = userId;
        this.betTime = betTime;
        this.orderId = orderId;
        this.sport = sport;
        this.leagueName = leagueName;
        this.homeName = homeName;
        this.awayName = awayName;
        this.betOption = betOption;
        this.handicapType = handicapType;
        this.instantHandicap = instantHandicap;
        this.homeCurrentScore = homeCurrentScore;
        this.awayCurrentScore = awayCurrentScore;
        this.betOdds = betOdds;
        this.betAmount = betAmount;
        this.validAmount = validAmount;
        this.resultAmount = resultAmount;
        this.betResult = betResult;
        this.homeScore = homeScore;
        this.awayScore = awayScore;
        this.proxy3Amount = proxy3Amount;
        this.proxy2Amount = proxy2Amount;
    }
}
