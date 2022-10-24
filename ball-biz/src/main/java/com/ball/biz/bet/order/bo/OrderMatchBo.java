package com.ball.biz.bet.order.bo;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

/**
 * @author lhl
 * @date 2022/10/21 下午6:51
 */
@Getter
@Setter
@Builder
public class OrderMatchBo {
    String oddsData;
    String matchId;
    String companyId;
    BigDecimal betOdds;
}
