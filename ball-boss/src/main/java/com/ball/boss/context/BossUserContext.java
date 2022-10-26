package com.ball.boss.context;


import com.ball.boss.service.system.model.UserInfo;

/**
 * @author JimChery
 */
public class BossUserContext {
    private static final ThreadLocal<UserInfo> user = new ThreadLocal<>();

    public static void set(UserInfo userInfo) {
        user.set(userInfo);
    }

    public static UserInfo get() {
        return user.get();
    }

    public static String getUserId() {
        UserInfo userInfo = user.get();
        return userInfo == null ? null : userInfo.getUserId();
    }

    public static String getUserName() {
        UserInfo userInfo = user.get();
        return userInfo == null ? null : userInfo.getUserName();
    }

    public static String getMobile() {
        UserInfo userInfo = user.get();
        return userInfo == null ? null : userInfo.getMobilePhone();
    }

    public static void remove() {
        user.remove();
    }
}
