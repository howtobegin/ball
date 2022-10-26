package com.ball.base.sync;

import java.util.HashMap;
import java.util.Map;

/**
 * 用户过多时可以考虑使用字符串池或则乐观锁
 * @author JimChery
 */
public class SyncObj {
    private static Map<String, String> LOCK = new HashMap<>();
    private static long last = System.currentTimeMillis();

    public static synchronized String getLock(String key) {
        // 如果上次调用时间超过一个小时，则直接清理LOCK
        long now = System.currentTimeMillis();
        if (now - last > 60 * 60 * 1000) {
            LOCK = new HashMap<>();
        }
        last = now;
        return LOCK.computeIfAbsent(key, k -> k);
    }
}
