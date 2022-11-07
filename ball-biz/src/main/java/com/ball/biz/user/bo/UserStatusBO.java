package com.ball.biz.user.bo;

import lombok.Getter;
import lombok.Setter;

/**
 * @author JimChery
 * @since 2022-11-07 17:35
 */
@Setter
@Getter
public class UserStatusBO {
    private Integer normalCount;

    private Integer lockCount;
}
