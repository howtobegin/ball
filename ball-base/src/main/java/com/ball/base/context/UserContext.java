package com.ball.base.context;


import com.ball.base.exception.BaseErrCode;
import com.ball.base.util.BizAssert;

/**
 * 用户信息上下文
 */
public class UserContext {
    private static final ThreadLocal<AppLoginUser> USER = new ThreadLocal<>();

    public static void set(AppLoginUser userNo) {
        USER.set(userNo);
    }

    public static Long getUserNo() {
        return get().getUserNo();
    }

    public static String getAccount() {
        return get().getAccount();
    }

    public static Integer getUserType() {
        return get().getUserType();
    }

    public static String getProxyInfo() {
        return get().getProxyInfo();
    }

    public static Long getProxyUid() {
        return get().getProxyUid();
    }

    public static void clear() {
        USER.remove();
    }

    private static AppLoginUser get() {
        AppLoginUser user = USER.get();
        BizAssert.notNull(user, BaseErrCode.INVALID_REQUEST);
        return user;
    }

}
