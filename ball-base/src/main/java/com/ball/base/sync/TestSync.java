package com.ball.base.sync;

import com.ball.base.util.SleepUtil;

/**
 * @author JimChery
 */
public class TestSync {
    public static void test() {
        // 3个线程执行用户1，3个线程执行用户2
        for (int i = 0; i < 6; i ++) {
            int count = i + 1;
            new Thread(() -> {
                String key = count % 2 == 0 ? "AAA" : "BBB";
                synchronized (SyncObj.getLock(key)) {
                    System.out.println("用户" + key + "-" + count + "进入");
                    // 先睡眠2s等待
                    SleepUtil.sleepSeconds(2);
                    System.out.println("用户" + key + "-" + count + "出去");
                }

            }).start();
        }

        for (int i = 0; i < 3; i++) {
            int count = i + 1;
            new Thread(() -> {
                synchronized (SyncObj.getLock("BBB")) {
                    System.out.println("混入BBB-" + count + "进入");
                    // 先睡眠2s等待
                    SleepUtil.sleepSeconds(2);
                    System.out.println("混入BBB-" + count + "出去");
                }

            }).start();
        }
        // 因为将锁清理调整为30s所有可以进入清理
        SleepUtil.sleepSeconds(30);
        // 3个线程执行用户3
        for (int i = 0; i < 3; i ++) {
            int count = i + 1;
            new Thread(() -> {
                synchronized (SyncObj.getLock("CCC")) {
                    System.out.println("用户CCC-" + count + "进入");
                    // 先睡眠2s等待
                    SleepUtil.sleepSeconds(2);
                    System.out.println("用户CCC-" + count + "出去");
                }

            }).start();
        }
    }

    public static void main(String[] args) {
        test();
    }
}
