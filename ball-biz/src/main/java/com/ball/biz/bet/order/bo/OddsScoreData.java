package com.ball.biz.bet.order.bo;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

/**
 * @author lhl
 * @date 2022/10/21 下午5:15
 */
@Getter
@Setter
@NoArgsConstructor
public class OddsScoreData {
    /**
     * 投注的bizNo
     */
    private String bizNo;

    private Integer homeScore;

    private Integer awayScore;

    private String odds;

    private String otherOdds;

    private List<OddsScoreItem> otherItems;

    @Getter
    @Setter
    public static class OddsScoreItem {
        private Long bizNo;

        /**
         * 1: prematch; 2: inplay
         */
        private Integer status;

        /**
         * 1: full; 2: half
         */
        private Integer type;

        private Integer homeScore;

        private Integer awayScore;

        private String odds;

        private String otherOdds;

        private Integer changeTime;
    }
}
