package com.ball.boss.controller.system.vo.response;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * @author littlehow
 */
@Getter
@Setter
public class GoogleSecretResp {

    @ApiModelProperty("密钥")
    private String googleSecret;

    @ApiModelProperty("google验证二维码字符串,根据这个数据生成需要的被google验证扫描的二维码")
    private String otpURL;
}
