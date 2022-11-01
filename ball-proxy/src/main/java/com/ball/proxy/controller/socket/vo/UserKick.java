package com.ball.proxy.controller.socket.vo;

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
