package com.ball.biz.bet.processor.bo;

import com.ball.biz.bet.enums.HandicapType;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * @author lhl
 * @date 2022/10/26 下午3:57
 */
@Getter
@Setter
public class OddsCheckInfo {
    private String matchId;
    /**
     * dataType(HANDICAP[让球全场],HANDICAP_HALF[让球半场],EUROPE_ODDS[独赢], OVER_UNDER[大小球全场],OVER_UNDER_HALF[大小球半场])
     */
    private HandicapType type;

    /**
     * 投注类型：1 早；2 今；3 滚
     */
    private Integer oddsType;

    /**
     * 是否维护
     */
    private boolean isMaintenance;

    /**
     * 是否关闭
     */
    private boolean isClose;

    /**
     * 赔率最近变化时间
     */
    private Integer latestChangeTime;

    /**
     * 接口最近更新时间
     */
    private LocalDateTime latestUpdateTime;

    @Builder
    public OddsCheckInfo(String matchId, HandicapType type, Integer oddsType, boolean isMaintenance, boolean isClose, Integer latestChangeTime, LocalDateTime latestUpdateTime) {
        this.matchId = matchId;
        this.type = type;
        this.oddsType = oddsType;
        this.isMaintenance = isMaintenance;
        this.isClose = isClose;
        this.latestChangeTime = latestChangeTime;
        this.latestUpdateTime = latestUpdateTime;
    }
}
