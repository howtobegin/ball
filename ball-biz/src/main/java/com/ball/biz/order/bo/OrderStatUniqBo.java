package com.ball.biz.order.bo;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

/**
 * @author lhl
 * @date 2022/11/4 下午6:01
 */
@Getter
@Setter
@Builder
public class OrderStatUniqBo {
    /**
     * 一级代理
     */
    private Long proxy1;

    /**
     * 二级代理
     */
    private Long proxy2;

    /**
     * 三级代理
     */
    private Long proxy3;

    /**
     * 投注日期
     */
    private LocalDate betDate;

    /**
     * 用户
     */
    private Long userId;

    /**
     * 投注币种
     */
    private String betCurrency;
}
