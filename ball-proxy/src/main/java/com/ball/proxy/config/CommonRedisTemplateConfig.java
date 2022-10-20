package com.ball.proxy.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;


/**
 * @Description:
 *
 * @Date: 2018/7/9
 * @Time: 14:46
 */
@Configuration
public class CommonRedisTemplateConfig {

    @Autowired
    private RedisConnectionFactory redisConnectionFactory;

    @Bean(name = "redisTemplate")
    public RedisTemplate redisTemplate() {
        RedisTemplate template = new RedisTemplate();
        template.setConnectionFactory(redisConnectionFactory);

        setKeySerializer(template);

        template.afterPropertiesSet();
        return template;
    }

    @Bean(name = "stringRedisTemplate")
    public StringRedisTemplate stringRedisTemplate() {
        StringRedisTemplate template = new StringRedisTemplate();
        template.setConnectionFactory(redisConnectionFactory);

        setStringKeySerializer(template);

        template.afterPropertiesSet();
        return template;
    }

    private void setKeySerializer(RedisTemplate template) {
        RedisSerializer<String> stringSerializer = new StringRedisSerializer();
        template.setKeySerializer(stringSerializer);
        template.setHashKeySerializer(stringSerializer);
    }

    private void setStringKeySerializer(StringRedisTemplate template) {
        RedisSerializer stringSerializer = new StringRedisSerializer();
        template.setKeySerializer(stringSerializer);
        template.setHashKeySerializer(stringSerializer);
    }

}
