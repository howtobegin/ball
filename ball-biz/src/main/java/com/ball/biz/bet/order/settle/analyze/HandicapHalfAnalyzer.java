package com.ball.biz.bet.order.settle.analyze;

import com.ball.biz.bet.enums.HandicapType;
import com.ball.biz.match.entity.Schedules;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * 让球半场
 *
 * 和全场区别：
 * 1、结算时期不一样
 * 2、获取主、客最后得分不一样
 *
 *
 * @author lhl
 * @date 2022/10/22 下午9:43
 */
@Slf4j
@Component
public class HandicapHalfAnalyzer extends HandicapAnalyzer {
    @Override
    public HandicapType getHandicapType() {
        return HandicapType.HANDICAP_HALF;
    }

    @Override
    protected Integer getHomeLastScore(Schedules schedules) {
        return schedules.getHomeHalfScore();
    }

    @Override
    protected Integer getAwayLastScore(Schedules schedules) {
        return schedules.getAwayHalfScore();
    }
}
