package com.ball.base.util;

import org.springframework.util.StringUtils;

import java.util.Base64;

/**
 * base64
 * 不支持中文
 */
public class SaltBase64Util {
    private static final byte[] SALT = {33,36,48,51,68,94};

    private static Base64.Decoder decoder = Base64.getDecoder();
    private static Base64.Encoder encoder = Base64.getEncoder();

    public static String decode(String target) {
        byte[] orig = decoder.decode(target);
        //首先获取长度
        int length = ByteNumberUtils.byte2int(orig);
        System.out.println(length);
        byte[] tb = new byte[length];
        int index = 0;
        int split = length / SALT.length;
        int saltSite = 0;
        int saltIndex = 0;
        //读取
        for (int i = 4, len = orig.length; i < len; i++) {
            if (index >= tb.length) {
                break;
            }
            tb[index++] = orig[i];
            saltSite++;
            if (saltSite >= split && saltIndex < SALT.length) {
                saltSite = 0;
                saltIndex++;
                i++;
            }
        }
        return new String(tb);
    }

    public static String encode(String target) {
        if (StringUtils.isEmpty(target)) {
            return target;
        }
        int index = 4;
        byte[] tb = target.getBytes();
        int length = tb.length;
        //合成数组总长度
        byte[] result = new byte[index + length + SALT.length];
        //先填充长度
        ByteNumberUtils.int2bytes(length, result);
        //判断间隔
        int split = length / SALT.length;
        int saltIndex = 0;
        int saltSite = 0;
        for (int i = 0; i < length; i++) {
            result[index++] = tb[i];
            //判断是否有间隔
            saltSite++;
            if (saltSite >= split && saltIndex < SALT.length) {
                saltSite = 0;
                result[index++] = SALT[saltIndex++];
            }
        }
        return encoder.encodeToString(result);
    }

    public static void main(String[] args) {
        String target = "1234";
        String encodeResult = encode(target);
        System.out.println(new String(decoder.decode(encodeResult)));
        System.out.println(encodeResult);
        String decodeResult = decode(encodeResult);
        System.out.println(target.equals(decodeResult));
        System.out.println(decodeResult);
    }

}
