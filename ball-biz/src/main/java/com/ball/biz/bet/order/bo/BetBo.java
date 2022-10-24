package com.ball.biz.bet.order.bo;

import com.ball.biz.bet.enums.BetOption;
import com.ball.biz.bet.enums.HandicapType;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

/**
 * @author lhl
 * @date 2022/10/20 下午3:56
 */
@Getter
@Setter
@Builder
public class BetBo {
    private Long userNo;

    private HandicapType handicapType;

    private String bizNo;

    private BetOption betOption;

    private BigDecimal betAmount;
}
