package com.ball.base.i18n;

import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.Locale;

public class LocalUtils {
    private final static String SPLIT_LINE = "-";
    private final static String PARAM_LANG = "lang";
    private final static String ACCEPT_LANGUAGE = "Accept-Language";
    private final static String HEADER_LANG_UP = "Lang";
    private final static String HEADER_LANG_LOW = "lang";


    public static Locale getLocale(HttpServletRequest request) {
        String headerUpLanguage = request.getHeader(HEADER_LANG_UP);
        String headerLowLanguage = request.getHeader(HEADER_LANG_LOW);
        String paramLanguage = request.getParameter(PARAM_LANG);
        String acceptLanguage = request.getHeader(ACCEPT_LANGUAGE);
        Locale locale;

        if (!StringUtils.isEmpty(headerUpLanguage)) {
            String[] splits = headerUpLanguage.split(SPLIT_LINE);
            locale = new Locale(splits[0], splits[1]);
        } else if (!StringUtils.isEmpty(headerLowLanguage)) {
            String[] splits = headerUpLanguage.split(SPLIT_LINE);
            locale = new Locale(splits[0], splits[1]);
        } else if (!StringUtils.isEmpty(paramLanguage)) {
            String[] splits = paramLanguage.split(SPLIT_LINE);
            locale = new Locale(splits[0], splits[1]);
        } else if (!StringUtils.isEmpty(acceptLanguage)) {
            acceptLanguage = acceptLanguage.split(",")[0];
            String[] splits = acceptLanguage.split(SPLIT_LINE);
            locale = new Locale(splits[0], splits[1]);
        } else {
            locale = new Locale("en", "US");
        }
        return locale;
    }
}
