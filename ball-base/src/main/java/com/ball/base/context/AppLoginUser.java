package com.ball.base.context;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

/**
 * @author littlehow
 */
@Setter
@Getter
@Accessors(chain = true)
public class AppLoginUser {
    private Long userNo;
}
