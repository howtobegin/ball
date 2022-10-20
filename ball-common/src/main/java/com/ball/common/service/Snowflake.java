package com.ball.common.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.Optional;

/**
 * A Snowflake like ID generator
 */
@Component
public class Snowflake {

    private static final String GLOBAL_NODE_PREFIX = "global:node:";
    protected static final int NODE_SHIFT = 8;
    protected static final int SEQ_SHIFT = 12;

    private static final short MAX_NODE = 256;
    private static final short MAX_SEQUENCE = 4096;

    private int node;
    private short sequence;
    private long referenceTime;

    @Value("${spring.application.name}")
    private String applicationName;

    @Autowired
    private StringRedisTemplate redisTemplate;

    public Snowflake() {}

    @PostConstruct
    public void init() {
        this.node =
            (int)(redisTemplate.opsForValue().increment(GLOBAL_NODE_PREFIX + applicationName, 1L).longValue() % MAX_NODE);
    }

    /**
     * 获取每个实例的节点 node
     * 
     * @return
     */
    public Integer getNodeId() {
        return Optional.ofNullable(node).orElseGet(() -> {
            init();
            return node;
        });
    }

    public synchronized long next() {

        long currentTime = System.currentTimeMillis();

        if (currentTime > referenceTime) {
            sequence = 1;
        } else {
            if (currentTime == referenceTime && sequence < Snowflake.MAX_SEQUENCE) {
                sequence++;
            } else {
                currentTime = waitNextMill();
                sequence = 0;
            }
        }
        referenceTime = currentTime;

        return currentTime << NODE_SHIFT << SEQ_SHIFT | node << SEQ_SHIFT | sequence;
    }

    private long waitNextMill() {
        long cur;
        do {
            cur = System.currentTimeMillis();
        } while (cur <= referenceTime);
        return cur;
    }
}
