package com.ball.common.service;

import com.warrenstrange.googleauth.GoogleAuthenticator;
import com.warrenstrange.googleauth.GoogleAuthenticatorConfig;
import com.warrenstrange.googleauth.GoogleAuthenticatorKey;
import com.warrenstrange.googleauth.GoogleAuthenticatorQRGenerator;
import com.ball.base.util.BizAssert;
import com.ball.common.bo.GoogleSecretBO;
import com.ball.common.exception.CommonErrorCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

/**
 * @author littlehow
 */
@Slf4j
@Service
public class GoogleAuthService {
    @Autowired
    private StringRedisTemplate redisTemplate;

    /**
     *  窗口大小
     */
    @Value("${google.auth.window.size:3}")
    private Integer googleAuthWindowSize;

    /**
     *  窗口时间
     */
    @Value("${google.auth.time.step:30}")
    private Integer timeStepSizeInSeconds;

    @Value("${google.auth.default.issuer:BALL}")
    private String defaultIssuer;

    @Value("${google.auth.default.issuer:@ball.com}")
    private String defaultAddr;

    /**
     * 谷歌验证器秘钥生成后过期时间（秒）,总共过期15分钟
     */
    private static final int GOOGLE_AUTH_TIME_OUT = 15;

    private String getGoogleAuthSecretKeyCacheKey(String userNo) {
        return "google:auth:" + userNo;
    }

    private String getGoogleAuthUrlCacheKey(String userNo) {
        return "google:authUrl:" + userNo;
    }


    /**
     * 根据用户生成google验证码信息，把用户和google验证码信息放入redis进行关联
     * @param message - 用户编号
     * @return -
     */
    public GoogleSecretBO getGoogleAuth(String message) {
        return getGoogleAuthInfo(message, defaultIssuer, defaultAddr);
    }

    private GoogleSecretBO getGoogleAuthInfo(String info, String issuer, String suffix) {
        //调用谷歌验证器 生成该用户的谷歌秘钥和认证路径
        GoogleAuthenticator googleAuthenticator = new GoogleAuthenticator();
        GoogleAuthenticatorKey googleAuthenticatorKey = googleAuthenticator.createCredentials();
        String googleSecret = googleAuthenticatorKey.getKey();
        String googleAuthURL = GoogleAuthenticatorQRGenerator.getOtpAuthTotpURL(issuer, info + suffix, googleAuthenticatorKey);
        //新生成的放入缓存
        redisTemplate.opsForValue().set(getGoogleAuthSecretKeyCacheKey(info), googleSecret, GOOGLE_AUTH_TIME_OUT, TimeUnit.MINUTES);
        redisTemplate.opsForValue().set(getGoogleAuthUrlCacheKey(info), googleAuthURL, GOOGLE_AUTH_TIME_OUT, TimeUnit.MINUTES);
        return new GoogleSecretBO().setGoogleSecret(googleSecret).setOtpURL(googleAuthURL);
    }

    /**
     * getGoogleAuthInfo生成userNo用户对应的googleSecret，在绑定的过程中传入userNo获取生成的googleSecret
     * @param message
     * @return
     */
    public String getGoogleSecretBoss(String message) {
        //从缓存中获取用户生成的密钥
        String googleSecret = redisTemplate.opsForValue().get(getGoogleAuthSecretKeyCacheKey(message));
        BizAssert.hasText(googleSecret, CommonErrorCode.GOOGLE_AUTH_TIMEOUT);
        return googleSecret;
    }

    /**
     *
     * @param googleSecret -
     * @param googleCheckCode 由getGoogleAuthInfo返回的googleSecret用户生成的谷歌验证码
     * @return
     */
    public void checkGoogleCode(String googleSecret, String googleCheckCode) {
        //验证谷歌验证码
        GoogleAuthenticatorConfig.GoogleAuthenticatorConfigBuilder configBuilder =
                new GoogleAuthenticatorConfig.GoogleAuthenticatorConfigBuilder()
                        .setTimeStepSizeInMillis(TimeUnit.SECONDS.toMillis(timeStepSizeInSeconds))
                        .setWindowSize(googleAuthWindowSize);
        GoogleAuthenticator googleAuthenticator = new GoogleAuthenticator(configBuilder.build());
        boolean checkResult = googleAuthenticator.authorize(googleSecret, Integer.valueOf(googleCheckCode));
        BizAssert.isTrue(checkResult, CommonErrorCode.GOOGLE_AUTH_CODE_ERROR);
    }
}
