package com.ball.common.bo;

import lombok.Getter;
import lombok.Setter;
import org.springframework.util.StringUtils;

/**
 * @author JimChery
 */
@Getter
@Setter
public class EmailGunResponse {
    private String id;
    private String message;

    public boolean success() {
        return StringUtils.hasText(id);
    }
}
