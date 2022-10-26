package com.ball.common.bo;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

/**
 * @author JimChery
 */
@Getter
@Setter
@Accessors(chain = true)
public class GoogleSecretBO {
    /**
     * 密钥
     */
    private String googleSecret;
    /**
     * google验证二维码字符串,根据这个数据生成需要的被google验证扫描的二维码
     */
    private String otpURL;
}
