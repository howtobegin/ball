package com.ball.biz.order.bo;

import com.ball.biz.bet.enums.OrderStatus;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

/**
 * @author lhl
 * @date 2022/10/27 下午3:02
 */
@Getter
@Setter
@NoArgsConstructor
public class OrderFinishBo {
    private String orderId;

    private OrderStatus pre;

    private BigDecimal resultAmount;

    private BigDecimal validAmount;

    private BigDecimal proxy1Amount;

    private BigDecimal proxy2Amount;

    private BigDecimal proxy3Amount;

    private BigDecimal backwaterAmount;

    @Builder
    public OrderFinishBo(String orderId, OrderStatus pre, BigDecimal resultAmount, BigDecimal validAmount, BigDecimal proxy1Amount, BigDecimal proxy2Amount, BigDecimal proxy3Amount, BigDecimal backwaterAmount) {
        this.orderId = orderId;
        this.pre = pre;
        this.resultAmount = resultAmount;
        this.validAmount = validAmount;
        this.proxy1Amount = proxy1Amount;
        this.proxy2Amount = proxy2Amount;
        this.proxy3Amount = proxy3Amount;
        this.backwaterAmount = backwaterAmount;
    }
}
