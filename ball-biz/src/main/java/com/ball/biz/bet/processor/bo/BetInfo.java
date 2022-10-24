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
    String oddsData;
    String matchId;
    String companyId;
    String betOddsStr;

    @Builder
    public BetInfo(String oddsData, String matchId, String companyId, String betOddsStr) {
        this.oddsData = oddsData;
        this.matchId = matchId;
        this.companyId = companyId;
        this.betOddsStr = betOddsStr;
    }
}
