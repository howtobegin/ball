package com.ball.app.controller.socket.vo;

import lombok.Getter;

/**
 * @author littlehow
 */
@Getter
public class UserKick {
    private String kickIp;

    public UserKick(String kickIp) {
        this.kickIp = kickIp;
    }
}
