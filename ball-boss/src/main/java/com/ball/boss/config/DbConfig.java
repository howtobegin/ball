package com.ball.boss.config;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Configuration
@ConfigurationProperties(prefix = "jdbc")
@Setter
@Getter
@Slf4j
public class DbConfig {
    @NotBlank
    private String url;

    @NotBlank
    private String username;

    @NotBlank
    private String password;

    @NotBlank
    private String driverClassName;

    @NotNull
    private Integer maxActive;

    @NotNull
    private Integer initialSize;

    @NotNull
    private Long maxWait;

    @NotNull
    private Integer minIdle;

    private Long timeBetweenEvictionRunsMillis = 30000L;

    private Long minEvictableIdleTimeMillis = 60000L;

    private String validationQuery = "SELECT 1 FROM DUAL";

    private Boolean testWhileIdle = true;

    @Value("${db.config.testOnBorrow:true}")
    private Boolean testOnBorrow;

    private Boolean removeAbandoned = true;

    private Integer removeAbandonedTimeoutSec = 1800;

    private Boolean logAbandoned = true;
}
