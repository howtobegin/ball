package com.ball.biz.socket.bo;

import lombok.Getter;

/**
 * @author JimChery
 */
@Getter
public class UserKick {
    private String kickIp;

    public UserKick(String kickIp) {
        this.kickIp = kickIp;
    }
}
