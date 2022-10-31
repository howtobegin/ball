package com.ball.biz.bet.processor.bo;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

/**
 * @author lhl
 * @date 2022/10/22 上午11:25
 */
@Setter
@Getter
public class BetInfo {
    private String oddsData;
    private String leagueId;
    private String matchId;
    private String companyId;
    private String betOddsStr;
    private String instantHandicap;
    private Integer oddsType;

    @Builder
    public BetInfo(String oddsData, String leagueId, String matchId, String companyId, String betOddsStr, String instantHandicap, Integer oddsType) {
        this.oddsData = oddsData;
        this.leagueId = leagueId;
        this.matchId = matchId;
        this.companyId = companyId;
        this.betOddsStr = betOddsStr;
        this.instantHandicap = instantHandicap;
        this.oddsType = oddsType;
    }
}
