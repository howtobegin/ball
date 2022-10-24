package com.ball.biz.bet.enums;

import com.ball.biz.exception.BizErrCode;
import com.ball.biz.exception.BizException;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 赔率类型
 * @author lhl
 * @date 2022/10/18 下午6:07
 */
@Getter
@AllArgsConstructor
public enum OddsType {
    NONE(0, "无类型数据"),
    EARLY_ODDS(1, "早盘"),
    INSTANT_ODDS(2, "赛前即时盘"),
    IN_PLAY_ODDS(3, "走地盘（滚球盘）")
    ;


    private Integer code;
    private String desc;

    public static OddsType parse(Integer code) {
        for (OddsType e : values()) {
            if (e.code.equals(code)) {
                return e;
            }
        }
        throw new BizException(BizErrCode.PARAM_ERROR_DESC, "code");
    }
}
