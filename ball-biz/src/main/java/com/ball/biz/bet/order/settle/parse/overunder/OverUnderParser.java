package com.ball.biz.bet.order.settle.parse.overunder;

import com.ball.biz.bet.enums.HandicapType;
import com.ball.biz.bet.order.settle.parse.Parser;
import com.ball.biz.bet.order.settle.parse.ParserHolder;
import com.ball.biz.bet.order.settle.parse.bo.OverUnderParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;

/**
 * @author lhl
 * @date 2022/10/24 上午10:28
 */
@Slf4j
public abstract class OverUnderParser implements Parser<OverUnderParam>, InitializingBean {
    @Override
    public HandicapType getHandicapType() {
        return HandicapType.OVER_UNDER;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        ParserHolder.register(getHandicapType(), getBetType(), this);
    }
}
