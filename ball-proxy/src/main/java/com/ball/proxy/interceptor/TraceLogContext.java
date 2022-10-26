package com.ball.proxy.interceptor;

/**
 * @author JimChery
 */
public class TraceLogContext {
    private static ThreadLocal<Long> userNo = new ThreadLocal<>();
    private static ThreadLocal<String> ip = new ThreadLocal<>();

    public static void setIp(String ipValue) {
        ip.set(ipValue);
    }

    public static String getIp() {
        return ip.get();
    }

    public static void setUserNo(Long userNo1) {
        userNo.set(userNo1);
    }

    public static Long getUserNo() {
        return userNo.get();
    }

    public static void clear() {
        userNo.remove();
        ip.remove();
    }
}
