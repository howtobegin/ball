package com.ball.biz.exception;

import com.ball.base.exception.IBizErrCode;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author scliuhailin@163.com
 * @date 2021/9/27 下午2:59
 */
@Getter
@AllArgsConstructor
public enum OtcErrCode implements IBizErrCode {

    // 公共提示
    UPDATE_FAIL("O9998", "update fail"),
    ;

    /**
     * 枚举编码
     */
    private String code;

    /**
     * 描述说明
     */
    private String desc;
}
