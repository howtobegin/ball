package com.ball.base.model;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * @author JimChery
 */
public class Const {
    public static final String TRACE_ID = "X-B3-TraceId";
    public static final String SPAN_ID = "X-B3-SpanId";
    public static final String EMPTY_STRING = "";
    public static final String SPACE_STRING = " ";
    public static final String RELATION_SPLIT = "#";

    public static final String CHAIN_DEFAULT = "TRON";
    public static final String CURRENCY_LEGER = "CNY";
    public static final String CURRENCY_USDT = "USDT";
    public static final String CURRENCY_USDT_BSC = "USDT_BSC";

    public static final BigDecimal HUNDRED = new BigDecimal(100);

    public static final String SYSTEM_OPERATOR = "system";
    public static final String CALLBACK_OPERATOR = "callback";
    public static final int USDT_SCALE = 4;
    public static final int USDT_DECIMAL = 6;
    public static final int RATE_SCALE = 4;
    public static final int FIAT_SCALE = 2;//
    public static final Long MERCHANT_USER_NO = 999999999L;
    public static final String MERCHANT_PAYMENT_SNAPSHOT_NO = "-1";
    public static final String EXPORT_ORDER_EMPTY_VALUE = "--";

    public static final String SESSION_DEFAULT = "SESSION";

    public static final LocalDate START_DATE = LocalDate.of(2000,1,1);

    public static boolean hasRelation(String relation, Long parent) {
        String[] info = relation.split(RELATION_SPLIT);
        String pp = parent.toString();
        for (String p : info) {
            if (pp.equals(p)) {
                return true;
            }
        }
        return false;
    }
}
