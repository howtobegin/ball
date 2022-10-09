package com.ball.base.util;

import org.springframework.util.StringUtils;

/**
 * @author littlehow
 */
public class StringUtil {
    private static final String DESEN = "***";
    private static final String KEY_DESEN = "...";
    public static String desensitization(String target) {
        if (target == null) return null;
        // 默认脱敏隐藏4位
        if (target.length() < 3) {
            return target + DESEN + target;
        } else if (target.length() < 5) {
            return target.substring(0, 2) + DESEN + target.substring(2);
        } else if (target.length() < 7) {
            return target.substring(0, 2) + DESEN + target.substring(target.length() - 2);
        } else {
            return target.substring(0, 3) + DESEN + target.substring(target.length() - 4);
        }
    }

    public static String desnsitizationEmail(String email) {
        if (StringUtils.isEmpty(email)){
            return null;
        }
        int index = email.indexOf("@");
        String prefix = email.substring(0, index);
        return desensitization(prefix) + email.substring(index);
    }

    public static String desenKey(String target) {
        if (target == null || target.length() < 15) {
            return target;
        }
        return target.substring(0, 4) + KEY_DESEN + target.substring(target.length() - 14);
    }

    public static String desenKey2(String target) {
        if (target == null || target.length() < 15) {
            return target;
        }
        return target.substring(0, 6) + KEY_DESEN + target.substring(target.length() - 8);
    }
}
