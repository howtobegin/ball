package com.ball.app.config;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.expression.BeanFactoryResolver;
import org.springframework.expression.BeanResolver;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;

/**
 * @author lhl
 * @date 2022/10/28 下午4:42
 */
@Configuration
public class AnnotationConfig {
    @Bean
    public ExpressionParser expressionParser() {
        return new SpelExpressionParser();
    }

    @Bean
    public BeanResolver beanResolver(ApplicationContext applicationContext) {
        return new BeanFactoryResolver(applicationContext);
    }
}
