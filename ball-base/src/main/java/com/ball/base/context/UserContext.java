package com.ball.base.context;


/**
 * 用户信息上下文
 */
public class UserContext {
    private static final ThreadLocal<AppLoginUser> USER = new ThreadLocal<>();

    public static void set(AppLoginUser userNo) {
        USER.set(userNo);
    }

    public static Long getUserNo() {
        AppLoginUser user = USER.get();
        return user == null ? null : user.getUserNo();
    }

    public static void clear() {
        USER.remove();
    }

}
