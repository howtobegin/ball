package com.ball.base.util;

import com.baomidou.mybatisplus.core.toolkit.Sequence;

/**
 * 使用mybatisplus内置的雪花算法来做id生成器
 * workerId和datacenterId可以基于数据库或redis等设备
 * 此处为单机部署，所以使用默认的mac地址算法和进程算法即可
 * @author JimChery
 */
public class IDCreator {
    /**
     * 分布式情况下可以自主设置workerId和datacenterId
     *
     * @see Sequence#Sequence(long, long)
     *
     */
    private static Sequence sequence = new Sequence();

    public static String get() {
        return sequence.nextId() + "";
    }

    public static long getLongId() {
        return sequence.nextId();
    }

    public static void main(String[] args) {
        System.out.println(get());
    }
}
