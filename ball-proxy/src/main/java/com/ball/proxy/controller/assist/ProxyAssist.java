package com.ball.proxy.controller.assist;

import com.ball.base.model.Const;
import org.springframework.util.StringUtils;

/**
 * @author JimChery
 * @since 2022-11-10 19:04
 */
public class ProxyAssist {
    public static String getLike(String proxyInfo, String current) {
        if (StringUtils.isEmpty(proxyInfo)) {
            return current + Const.SQL_LIKE;
        }
        return proxyInfo + Const.RELATION_SPLIT + current + Const.SQL_LIKE;
    }
}
