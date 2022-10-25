package com.ball.biz.socket.bo;

/**
 * @author littlehow
 */
public enum SocketDataType {
    // 用户登录踢出
    USER_LOGIN_KICK(101);
    public final int code;

    SocketDataType(int code) {
        this.code = code;
    }
}
