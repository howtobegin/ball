package com.ball.common.bo;

import lombok.Getter;
import lombok.Setter;
import org.springframework.util.StringUtils;

/**
 * @author JimChery
 */
@Setter
@Getter
public class SmsResponse {
    private String status;

    private String messageid;


    public boolean success() {
        return ResponseCode.SUCCESS.isMe(status) && StringUtils.hasText(messageid);
    }

    enum ResponseCode {
        SUCCESS("0", "成功"),
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



