package com.ball.biz.bet.match;

import com.ball.biz.bet.match.bo.OddsBo;
import com.ball.biz.match.entity.Schedules;

/**
 * @author lhl
 * @date 2022/11/3 下午3:23
 */
public interface Controller {

    boolean show(Schedules schedules, OddsBo odds);

    boolean close(Schedules schedules, OddsBo odds);
}
