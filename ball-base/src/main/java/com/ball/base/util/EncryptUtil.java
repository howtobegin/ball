package com.ball.base.util;

import org.springframework.util.StringUtils;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class EncryptUtil {

    private static final char[] MD5_CHAR = "0123456789abcdef".toCharArray();

    public static String md5(String content) {
        return encrypt(content, "MD5");
    }

    public static String sha1(String content) {
        return encrypt(content, "SHA1");
    }

    public static String encrypt(String content, String protocol) {
        if (StringUtils.isEmpty(content)) {
            throw new IllegalArgumentException("String to encrypt cannot be null or zero length");
        }
        StringBuilder result = new StringBuilder();
        try {
            MessageDigest md = MessageDigest.getInstance(protocol);
            md.update(content.getBytes());
            byte[] hash = md.digest();
            for (byte b : hash) {
                result.append(MD5_CHAR[b >> 4 & 0xF]);
                result.append(MD5_CHAR[b & 0xF]);
            }
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalArgumentException("NoSuchAlgorithmException", e);
        }
        return result.toString();
    }

    /**
     * 增强md5
     * @param content -
     * @return -
     */
    public static String md5Plus(String content) {
        return md5(getSaltContent(md5(content)));
    }


    public static String uriEncode(String v) {
        try {
            return URLEncoder.encode(v, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            // skip
            return v;
        }
    }

    /**
     * @param content -
     * @return -
     */
    private static String getSaltContent(String content) {
        if (StringUtils.isEmpty(content)) {
            throw new IllegalArgumentException("String to encrypt cannot be null or zero length");
        }
        return "bal" + content + "l-salt";
    }

    public static void main(String[] args) {
        System.out.println(md5("111111"));
        System.out.println(md5Plus("111111"));
        System.out.println(sha1("111111"));
        System.out.println(md5("bce36bce3637c5595b90739c0015663f7bc47bcaef2d9897a1390d528eb7dafd45e0445e04"));
    }
}
