package com.ball.biz.bet.order.bo;

import com.ball.biz.bet.enums.BetType;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

/**
 * @author lhl
 * @date 2022/10/22 上午9:21
 */
@Setter
@Getter
public class Handicap {
    private BetType betType;

    private Double big;

    /**
     * 全投small可能null
     */
    private Double small;

    @Builder
    public Handicap(BetType betType, Double small, Double big) {
        this.betType = betType;
        this.small = small;
        this.big = big;
    }
}
