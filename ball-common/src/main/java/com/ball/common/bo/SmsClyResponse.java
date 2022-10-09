package com.ball.common.bo;

import lombok.Getter;
import lombok.Setter;
import org.springframework.util.StringUtils;

/**
 * @author littlehow
 */
@Setter
@Getter
public class SmsClyResponse {
    private String resultCode;

    private String msgid;

    private String seqid;

    private String resultMsg;

    public boolean success() {
        return ResponseCode.SUCCESS.isMe(resultCode) && StringUtils.hasText(msgid);
    }

    enum ResponseCode {
        SUCCESS("1", "成功"),
        FAIL("0", "失败"),
        USER_PASSWORD_ERROR("-1", "用户名或者密码不正确"),
        BALANCE_NOT_ENOUGH("-5", "余额不够")
        ;
        public final String code;
        public final String message;
        ResponseCode(String code, String message) {
            this.code = code;
            this.message = message;
        }

        public boolean isMe(String code) {
            return this.code.equals(code);
        }
    }
}



