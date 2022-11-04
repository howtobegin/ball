package com.ball.biz.bet.processor.cache;

import com.ball.biz.account.entity.UserAccount;
import com.ball.biz.match.entity.Schedules;

/**
 * check的时候已经查询了某些对象，缓存一下，后面用
 * @author lhl
 * @date 2022/10/31 下午5:05
 */
public class BetCache {
    private static final ThreadLocal<Schedules> SCHEDULE = new ThreadLocal<>();
    private static final ThreadLocal<UserAccount> USER_ACCOUNT = new ThreadLocal<>();

    public static void setSchedule(Schedules data) {
        SCHEDULE.set(data);
    }
    public static Schedules getSchedule() {
        return SCHEDULE.get();
    }

    public static void setUserAccount(UserAccount data) {
        USER_ACCOUNT.set(data);
    }
    public static UserAccount getUserAccount() {
        return USER_ACCOUNT.get();
    }

    public static void clear() {
        SCHEDULE.remove();
        USER_ACCOUNT.remove();
    }
}
