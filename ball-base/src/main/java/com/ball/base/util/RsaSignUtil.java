package com.ball.base.util;

import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.binary.Base64;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.*;

@Slf4j
public class RsaSignUtil {

    /** charset */
    private final static String INPUT_CHARSET = "UTF-8";
    public static final String KEY_ALGORITHM = "RSA";
    public static final String SIGNATURE_ALGORITHM = "SHA256withRSA";
    private static final String SIGN = "sign";
    private static final String EMPTY = "";

    public static final String PRIVATE_KEY="MIIEvQIBADANBgkqhkiG9w0BAQEFAASCBKcwggSjAgEAAoIBAQCGv61ElyTaEOsQWa/rxX85VMS/JB5lbqx2wVYfxGq15dJ7c84/dvL+0Mqp6C5g+mQqADB/bB6ZTT3SpFCJbYh23NqJ+QTdDTYpM5mbIP7C1WpBNyltT6oXBDjAZ4AKL/ntEvWDktvt/3Atl47GbZLSagDeCZDGdkBJwWxqWQ+YgY7lL2IH93X/qXa06UorLPzwVQNZnE4mM0Noss+YEilzlJmPtPHYNDAlgzJxQHebsn1dEwdS8fW4k3hnHxx/mcHcL2GdvhQ0XNF+zMJNfmZpAXpV7pl7olsq9IXqU95HAhpR/aFJtgeH09dMeYNX4tIKBS4SBRgthzL3mSvkDmazAgMBAAECggEAc6IpXqMVzMyX7AALJ5jkBJ7p991W+FRxS7e94KT9rfr7FVXbuDqrbGBgik3k/vPg4OootDBkUconM4wuUMDeSqJcJ+q6cbfUsqbFOoqmFSxvcmeT9WpX3EiTKwmmpxdksj2C0tZERYjfC0D0FPQS+t5cwC4VPzpbEPAbyWeEKqpAmR+lH/1cqDH/BiaI1WTY5il03ynLnRrHYxS3ZvGS7dMOkclOzhGlySIz/wzfprIcG6CUJOnjcTETK1JvRh2VhsexfJRdlpxnJ4YQffdsVCsrXDWrlJg/fCEeeWVQE0Uuv9uNjwlBJrxT3Yikao7q/C2GMif0aFq/OdqrHywqgQKBgQDawzXzXOPj6yu6bZDLPFtRVEAJL25NXA/SBDkOSw0l39OiEhrX3W8SeXMdGp/zcM8J30oEN+z5FLgFEF3czL8blA1hsezCb/1OYGi9t6qyZ2YPy2remE9xPPZuKku9yb49wFfGGpvqxYQFDIXSEZ33TDXcLqyTJnaJGkIF9euF5wKBgQCdr3u8BCMZTSja1JyrhhlX8y1AoCvqLkBovEEeLgaSQVrf3xYhJ/7tC4HuS6AodRhQQGdrhRHpp1Uhfllqs+NWE0U2TmdgkxgTTkDN3vxeYJ33p0csF9FBq7a+RCEjN8K5ORyU//MS//Alc1BAX0679NTFeM2HW1WG4ooIwM1nVQKBgFZ/Y0CjlPQUk4i2M884QT8qGdvH5kwuP48h3lF5eWxlUO+/St/ZjUZ3wP1b/qyqCSL1J/X9CvT9rTlLXGGmj1XfN19Ok7fZwgWQdzm0eojp+1/5ZXs4LAkxOpsanDIhp9gQymPpN4cfq/C3H96M2rHib1VtGw1f4vSY7kwfDY43AoGANEdZ3pwAhwLt6J9U0glDncUh26s+5AUxlYfUjwzzYMoKNu7fh16bRjvqd/bawo8P4It5vkwABFdWAiTBwH6jgIYjDD4yR/J8qOno0lh7k061EMaE8jlzRzKHA83Srrpt9Lranwu8qy/vjnsMwErY77fcUxwaCCZf1LiGyyAg+YECgYEA1TorTz0b+qhv4D2jBCSt0n5h2U1tP4aJe2XMAMSemvG8va/FQvXZadtISu5xHLu4wBc+a+Cd0j9++ipo7X4608gmFpRJw71DysQOVMUE0dPbjKNXqoeOLVB0gSMdwOHwpQZM0Rc+FbLK4PEaCCTbuUa+QP5D5jX74C58xGrAr9k=";
    public static final String PUBLIC_KEY ="MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAhr+tRJck2hDrEFmv68V/OVTEvyQeZW6sdsFWH8RqteXSe3POP3by/tDKqeguYPpkKgAwf2wemU090qRQiW2IdtzaifkE3Q02KTOZmyD+wtVqQTcpbU+qFwQ4wGeACi/57RL1g5Lb7f9wLZeOxm2S0moA3gmQxnZAScFsalkPmIGO5S9iB/d1/6l2tOlKKyz88FUDWZxOJjNDaLLPmBIpc5SZj7Tx2DQwJYMycUB3m7J9XRMHUvH1uJN4Zx8cf5nB3C9hnb4UNFzRfszCTX5maQF6Ve6Ze6JbKvSF6lPeRwIaUf2hSbYHh9PXTHmDV+LSCgUuEgUYLYcy95kr5A5mswIDAQAB";

    /**
     * RSA签名
     *
     * @param object        待签名对象
     * @param privateKeyStr 私钥
     * @return 签名值
     */
    public static String sign(Object object, String privateKeyStr) {
        try {
            Map<String, String> map = convertToMap(object);
            //create pre sign str
            String content = createLinkString(map);
            //解密私钥
            byte[] keyBytes = decryptBase64(privateKeyStr);
            PKCS8EncodedKeySpec priPKCS8 = new PKCS8EncodedKeySpec(keyBytes);
            //指定加密算法
            KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
            //取私钥匙对象
            PrivateKey privateKey = keyFactory.generatePrivate(priPKCS8);
            //用私钥对信息生成数字签名
            Signature signature = Signature.getInstance(SIGNATURE_ALGORITHM);
            signature.initSign(privateKey);
            signature.update(content.getBytes(StandardCharsets.UTF_8));
            byte[] sig = signature.sign();
            return encryptBase64(sig).replaceAll("\n", "");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String signMap(HashMap map, String privateKeyStr) {
        try {
            //create pre sign str
            String content = createLinkString(map);
            //解密私钥
            byte[] keyBytes = decryptBase64(privateKeyStr);
            PKCS8EncodedKeySpec priPKCS8 = new PKCS8EncodedKeySpec(keyBytes);
            //指定加密算法
            KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
            //取私钥匙对象
            PrivateKey privateKey = keyFactory.generatePrivate(priPKCS8);
            //用私钥对信息生成数字签名
            Signature signature = Signature.getInstance(SIGNATURE_ALGORITHM);
            signature.initSign(privateKey);
            signature.update(content.getBytes(StandardCharsets.UTF_8));
            byte[] sig = signature.sign();
            return encryptBase64(sig).replaceAll("\n", "");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 公钥验证签名
     *
     * @param sign
     * @param object
     * @param puklicKeyStr
     * @return
     */
    public static boolean verify(String sign, Object object, String puklicKeyStr) {
        byte[] pubkeyBytes = decryptBase64(puklicKeyStr);
        X509EncodedKeySpec pubkey = new X509EncodedKeySpec(pubkeyBytes);
        Map<String, String> map = convertToMap(object);
        //create pre sign str
        String content = createLinkString(map);
        try {
            //指定加密算法
            KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
            PublicKey publicKey = keyFactory.generatePublic(pubkey);
            Signature signature = Signature.getInstance(SIGNATURE_ALGORITHM);
            signature.initVerify(publicKey);
            signature.update(content.getBytes(StandardCharsets.UTF_8));
            return signature.verify(decryptBase64(sign));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }


    /**
     * 将Object对象里面的属性和值转化成Map对象
     */
    public static Map<String, String> convertToMap(Object obj) {
        //caution: json转化过程中会添加null字段. 还需要注意全局化json配置对这里的影响。
        //filter empty value、sign
        return paraFilter(JSON.parseObject(JSON.toJSONString(obj)));
    }


    private static Map<String, String> paraFilter(Map<String, Object> sArray) {
        Map<String, String> result = new HashMap<>();
        if (sArray == null || sArray.size() <= 0) {
            return result;
        }
        for (String key : sArray.keySet()) {
            Object value = sArray.get(key);
            if (value == null || EMPTY.equals(value) || SIGN.equalsIgnoreCase(key)) {
                continue;
            }
            String v1;
            if (value instanceof BigDecimal) {
                v1 = ((BigDecimal) value).stripTrailingZeros().toPlainString();
            } else if (value instanceof Long){
                v1 = ""+value;
            } else {
                v1 = value.toString();
            }
            result.put(key, v1);
        }
        return result;
    }

    private static String createLinkString(Map<String, String> params) {
        return createLinkString(params, false);
    }

    private static String createLinkString(Map<String, String> params, boolean encode) {
        List<String> keys = new ArrayList<>(params.keySet());
        Collections.sort(keys);
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < keys.size(); i++) {
            String key = keys.get(i);
            String value = params.get(key);
            if (encode) {
                value = urlEncode(value, INPUT_CHARSET);
            }
            //拼接时，不包括最后一个&字符
            if (i == keys.size() - 1) {
                stringBuilder.append(key).append("=").append(value);
            } else {
                stringBuilder.append(key).append("=").append(value).append("&");
            }
        }
        return stringBuilder.toString();
    }


    private static String urlEncode(String content, String charset) {
        try {
            return URLEncoder.encode(content, charset);
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException("encoding fail :" + charset);
        }
    }

    /**
     * BASE64解密
     */
    private static byte[] decryptBase64(String key) {
        return Base64.decodeBase64(key);
    }

    /**
     * BASE64加密
     */
    private static String encryptBase64(byte[] key) {
        return Base64.encodeBase64String(key);
    }


}
