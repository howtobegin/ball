package com.ball.biz.bet.order.bo;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @author lhl
 * @date 2022/10/21 下午2:32
 */
@Getter
@Setter
@NoArgsConstructor
public class OddsData {
    private String bizNo;

    private String initialHandicap;

    private String initialHome;

    private String initialAway;

    private String initialDraw;

    private String initialOver;

    private String initialUnder;

    private String instantHandicap;

    private String instantHome;

    private String instantAway;

    private String instantDraw;

    private String instantOver;

    private String instantUnder;

    /**
     * Is there inplay odds?
     */
    private Boolean inPlay;

    /**
     * When the handicapIndex is 1, it is the data of the main market.
     */
    private Integer handicapIndex;

    /**
     * Change time, unix timestamp
     */
    private Integer changeTime;

    /**
     * 0:Unable to judge 1:Early Odds 2:Instant odds(after the early odds before the match) 3:Inplay odds
     */
    private Integer oddsType;

    @Builder
    public OddsData(String initialHandicap, String initialHome, String initialAway, String initialDraw, String initialOver, String initialUnder, String instantHandicap, String instantHome, String instantAway, String instantDraw, String instantOver, String instantUnder, Boolean inPlay, Integer handicapIndex, Integer changeTime, Integer oddsType) {
        this.initialHandicap = initialHandicap;
        this.initialHome = initialHome;
        this.initialAway = initialAway;
        this.initialDraw = initialDraw;
        this.initialOver = initialOver;
        this.initialUnder = initialUnder;
        this.instantHandicap = instantHandicap;
        this.instantHome = instantHome;
        this.instantAway = instantAway;
        this.instantDraw = instantDraw;
        this.instantOver = instantOver;
        this.instantUnder = instantUnder;
        this.inPlay = inPlay;
        this.handicapIndex = handicapIndex;
        this.changeTime = changeTime;
        this.oddsType = oddsType;
    }
}
