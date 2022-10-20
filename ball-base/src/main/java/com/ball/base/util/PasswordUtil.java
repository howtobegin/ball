package com.ball.base.util;

/**
 * @author littlehow
 */
public class PasswordUtil {
    public static String get(String password) {
        return EncryptUtil.md5Plus(password);
    }

    public static boolean checkPass(String des, String enc) {
        return EncryptUtil.md5Plus(des).equals(enc);
    }

    public static boolean checkValid(String password) {
        // 密码需要包含大小写字母、数字，长度必须8-16位
        int length = password.length();
        if (length < 8 || length > 16) {
            return false;
        }
        // 去掉数字
        password = password.replaceAll("\\d", "");
        int length1 = password.length();
        if (length == length1) {
            return false;
        }
        // 去掉小写字母
        password = password.replaceAll("[a-z]", "");
        length = password.length();
        if (length == length1) {
            return false;
        }
        // 去掉大写字母
        password = password.replaceAll("[A-Z]", "");
        length1 = password.length();
        if (length == length1 || length1 != 0) {
            return false;
        }
        return true;
    }

    public static void main(String[] args) {
        System.out.println(get("Aa123456"));
    }
}
