package com.ball.biz.bet.order.settle.parse.handicap;

import com.ball.biz.bet.enums.HandicapType;
import com.ball.biz.bet.order.settle.parse.Parser;
import com.ball.biz.bet.order.settle.parse.ParserHolder;
import com.ball.biz.bet.order.settle.parse.bo.HandicapParam;
import org.springframework.beans.factory.InitializingBean;

/**
 * @author lhl
 * @date 2022/10/23 上午8:56
 */
public abstract class HandicapParser implements Parser<HandicapParam>, InitializingBean {
    @Override
    public HandicapType getHandicapType() {
        return HandicapType.HANDICAP;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        ParserHolder.register(getHandicapType(), getBetType(), this);
    }
}
