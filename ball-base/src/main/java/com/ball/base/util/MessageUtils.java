package com.ball.base.util;


import org.springframework.util.CollectionUtils;

import java.util.Map;

/**
 * @author littlehow
 */
public class MessageUtils {

    /**
     * 格式化消息
     * @param orig    -
     * @param params  -
     * @return        -
     */
    public static String format(String orig, Map<String, String> params) {
        if (CollectionUtils.isEmpty(params)) {
            return orig;
        }
        for (String key : params.keySet()) {
            orig = orig.replace("{" + key + "}", params.get(key));
        }
        return orig;
    }
}
