package com.ball.base.util;

import javax.servlet.http.HttpServletRequest;

public class IpUtil {
    public static String realIP(HttpServletRequest request) {
        String xff = request.getHeader("x-forwarded-for");
        if (xff != null) {
            int index = xff.indexOf(',');
            if (index != -1) {
                xff = xff.substring(0, index);
            }
            return xff.trim();
        }
        return request.getRemoteAddr();
    }
}
