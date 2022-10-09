package com.ball.common.http;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class HttpBaseConfig {
    /**
     * 验签模式，true开启验签
     */
    private boolean signMode;

    private String privateKey;

    private String publicKey;

    private String baseUrl;


}
