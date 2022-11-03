package com.ball.biz.bet.match.bo;

import com.ball.biz.bet.enums.HandicapType;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

/**
 * @author lhl
 * @date 2022/11/3 下午3:28
 */
@Getter
@Setter
@Builder
public class OddsBo {
    private HandicapType handicapType;

    private Boolean isClose;
    /**
     * 0:Unable to judge 1:Early Odds 2:Instant odds(after the early odds before the match) 3:Inplay odds
     * 波胆：1 prematch；2 inplay
     */
    private Integer oddsType;

    private String instantHandicap;
}
