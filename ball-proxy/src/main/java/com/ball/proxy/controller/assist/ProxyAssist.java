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
        String[] info = proxyInfo.split(Const.RELATION_SPLIT);
        boolean hasMe = false;
        StringBuilder sb = new StringBuilder();
        for (String s : info) {
            sb.append(s);
            if (s.equals(current)) {
                hasMe = true;
                break;
            }
            sb.append(Const.RELATION_SPLIT);
        }
        if (!hasMe) {
            sb.append(current);
        } else {
            sb.append(Const.SQL_LIKE);
        }
        return sb.toString();
    }
}
