package com.ball.biz.bet.order.bo;

import com.ball.biz.bet.enums.BetOption;
import com.ball.biz.bet.enums.HandicapType;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * @author lhl
 * @date 2022/10/20 下午3:56
 */
@Getter
@Setter
@NoArgsConstructor
public class BetBo {
    private Long userNo;

    private HandicapType handicapType;

    private String bizNo;

    private BetOption betOption;

    private BigDecimal betAmount;

    private String matchId;

    private LocalDateTime betTime;

    @Builder
    public BetBo(Long userNo, HandicapType handicapType, String bizNo, BetOption betOption, BigDecimal betAmount, String matchId, LocalDateTime betTime) {
        this.userNo = userNo;
        this.handicapType = handicapType;
        this.bizNo = bizNo;
        this.betOption = betOption;
        this.betAmount = betAmount;
        this.matchId = matchId;
        this.betTime = betTime;
    }
}
