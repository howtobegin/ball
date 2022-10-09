package com.ball.base.util;

import java.util.Random;
import java.util.concurrent.TimeUnit;

/**
 * @author littlehow
 */
public class SleepUtil {

    private static Random random = new Random();

    public static void sleepSeconds(long seconds) {
        try {
            TimeUnit.SECONDS.sleep(seconds);
        } catch (InterruptedException e) {
            // skip
        }
    }

    public static void sleepSeconds(int start, int end) {
        int time = random.nextInt(end - start + 1) + start;
        sleepSeconds(time);
    }
}
