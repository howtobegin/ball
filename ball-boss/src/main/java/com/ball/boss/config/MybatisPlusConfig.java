

package com.ball.boss.config;

import lombok.extern.slf4j.Slf4j;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Configuration;


@Configuration
@MapperScan({"com.ball.biz.*.mapper", "com.ball.wallet.mapper", "com.ball.boss.dao.mapper", "com.ball.boss.dao.ext"})
@Slf4j
public class MybatisPlusConfig {

}
