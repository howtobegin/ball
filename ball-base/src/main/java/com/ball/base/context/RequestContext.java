package com.ball.base.context;


import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;

/**
 * 用户信息上下文
 */
@Slf4j
public class RequestContext {
    private static final ThreadLocal<RequestInfo> request = new ThreadLocal<>();

    private static String DEFAULT_LANG = "zh-CN";

    public static void setDefaultLang(String lang) {
        DEFAULT_LANG = lang;
    }

    public static void set(RequestInfo requestInfo) {
        request.set(requestInfo);
    }

    public static RequestInfo get() {
        return request.get();
    }

    public static String getIp() {
        RequestInfo requestInfo = request.get();
        return requestInfo == null ? null : requestInfo.getIp();
    }

    public static String getTerminal() {
        RequestInfo requestInfo = request.get();
        return requestInfo == null ? null : requestInfo.getTerminal();
    }

    public static String getReferer() {
        RequestInfo requestInfo = request.get();
        return requestInfo == null ? null : requestInfo.getReferer();
    }

    public static String getLang() {
        RequestInfo requestInfo = request.get();
        if (requestInfo == null) {
            return DEFAULT_LANG;
        }
        String lang = requestInfo.getLang();
        log.info("lang {}",lang);
        if (StringUtils.hasText(lang)) {
            return lang;
        } else {
            return DEFAULT_LANG;
        }
    }

    public static void remove() {
        request.remove();
    }



    public static boolean sameDomain(String domain) {
        String referer = getReferer();
        if (referer == null || domain == null) {
            return false;
        }
        domain = domain.replace("/", "");
        return referer.endsWith(domain);
    }

    public static boolean isDefaultLang() {
        return DEFAULT_LANG.equalsIgnoreCase(getLang());
    }
}
