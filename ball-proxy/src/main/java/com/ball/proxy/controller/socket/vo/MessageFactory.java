package com.ball.proxy.controller.socket.vo;

import com.alibaba.fastjson.JSONObject;

/**
 * @author JimChery
 */
public class MessageFactory {
    public static String getUserKick(String ip) {
        SocketData data = new SocketData();
        data.setType(SocketDataType.USER_LOGIN_KICK.code);
        data.setData(new UserKick(ip));
        return JSONObject.toJSONString(data);
    }
}
