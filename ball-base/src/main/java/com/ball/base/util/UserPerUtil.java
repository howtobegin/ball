package com.ball.base.util;

import org.springframework.data.redis.core.RedisTemplate;


/**
 * @author littlehow
 */
public class UserPerUtil {

    private static String serviceKey = "self:server:timeout";

    private static RedisTemplate<String, Object> template;

    public static void setTemplate(RedisTemplate<String, Object> template) {
        UserPerUtil.template = template;
    }

    public static boolean getService() {
        Object obj = template.opsForValue().get(serviceKey);
        if (obj == null) {
            return true;
        } else {
            return "true".equals(obj.toString());
        }
    }

    public static void closeService() {
        template.opsForValue().set(serviceKey, "false");
    }

    public static void openService() {
        template.opsForValue().set(serviceKey, "true");
    }
}
