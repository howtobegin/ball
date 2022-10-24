package com.ball.biz.bet.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 *
 * @author lhl
 * @date 2022/10/24 下午3:31
 */
@Getter
@AllArgsConstructor
public enum MatchTimeType {
    // 整场
    FULL(1),
    // 半场
    HALF(2);

    private Integer code;

    public boolean isMe(Integer code) {
        return code != null && code.equals(this.code);
    }
}
