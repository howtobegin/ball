package com.ball.biz.bet.match;

import com.ball.biz.bet.enums.HandicapType;
import com.ball.biz.bet.enums.ScheduleStatus;
import com.ball.biz.bet.match.bo.OddsBo;
import com.ball.biz.match.entity.Schedules;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * @author lhl
 * @date 2022/11/3 下午3:57
// */
@Slf4j
@Component
public class CommonController implements Controller {
    @Value("${match.close.time:2640}")
    private int halfCloseTime;

    @Override
    public boolean show(Schedules schedules, OddsBo odds) {
        Integer status = schedules.getStatus();
        boolean notStarted = ScheduleStatus.NOT_STARTED.isMe(status);
        // 如果未开始，全显示
        if (notStarted) {
            return true;
        }
        HandicapType handicapType = odds.getHandicapType();
        // 已开始，波胆，不显示
        switch (handicapType) {
            case CORRECT_SCORE:
            case CORRECT_SCORE_HALL:
                return false;
        }
        // 半场结束，半场的不显示
        boolean half = !handicapType.full();
        boolean halfFinish = status < 0 || status > ScheduleStatus.FIRST_HALF.getCode();
        if (half && halfFinish) {
            return false;
        }
        return true;
    }

    @Override
    public boolean close(Schedules schedules, OddsBo odds) {
        // 如果已关闭，直接返回
        if (Boolean.TRUE.equals(odds.getIsClose())) {
            return true;
        }
        HandicapType handicapType = odds.getHandicapType();
        // 判定比赛状态是否应该关闭
        Integer status = schedules.getStatus();
        if (!canBetCodes(handicapType).contains(status)) {
            return true;
        }
        // 判定比赛时间，是否应该不关闭
        Integer halfStartTime = schedules.getHalfStartTime();
        if (halfStartTime != null) {
            int diffSecond = LocalDateTime.now().getSecond() - halfStartTime;
            if (diffSecond > halfCloseTime) {
                return true;
            }
        }
        // 必赢赔率，直接close
        if (HandicapType.OVER_UNDER == handicapType || HandicapType.OVER_UNDER_HALF == handicapType) {
            BigDecimal score = new BigDecimal(schedules.getHomeScore() + schedules.getAwayScore());
            BigDecimal instantHandicap = new BigDecimal(odds.getInstantHandicap());
            // 如果当前比分大于即时盘口，就是必赢投注，需要关闭
            if (instantHandicap.compareTo(BigDecimal.ZERO) > 0 && score.compareTo(instantHandicap) > 0) {
                return false;
            }
        }

        return false;
    }

    protected List<Integer> canBetCodes(HandicapType type) {
        return type.full() ? ScheduleStatus.canBetCodes() : ScheduleStatus.halfCanBetCodes();
    }
}
