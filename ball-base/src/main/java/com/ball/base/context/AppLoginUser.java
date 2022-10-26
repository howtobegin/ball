package com.ball.base.context;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

/**
 * @author JimChery
 */
@Setter
@Getter
@Accessors(chain = true)
public class AppLoginUser {
    private Long userNo;

    private String account;

    private String userName;

    private String loginAccount;

    private Integer userType;

    private String proxyInfo;

    private Long proxyUid;
}
