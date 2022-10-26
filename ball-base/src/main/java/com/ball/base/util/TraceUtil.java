package com.ball.base.util;

import com.ball.base.model.Const;
import org.slf4j.MDC;

/**
 * 目前没有异步开线程处理业务，所以暂时不需要用spanId做区别
 * @author JimChery
 */
public class TraceUtil {
    private static String getTraceId() {
        return System.currentTimeMillis() + SimpleCodeUtil.getVerifyCode(3);
    }

    public static void start() {
        MDC.put(Const.TRACE_ID, getTraceId());
    }

    public static void end() {
        MDC.remove(Const.TRACE_ID);
    }
}
