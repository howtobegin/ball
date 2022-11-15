package com.ball.app.config;

import com.ball.app.interceptor.LoginInterceptor;
import com.ball.app.interceptor.RequestInterceptor;
import com.ball.base.web.JsonConfig;
import com.ball.base.web.SelfHttpMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.AbstractJackson2HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.ArrayList;
import java.util.List;

@Configuration
public class InterceptorConfig implements WebMvcConfigurer {

    private static final String ALL = "/app/**";
    private static final String USER = "/app/user/**";
    private static final String ORDER = "/app/order/**";
    private static final String LEAGUE = "/app/league/**";
    private static final String ODDS = "/app/odds/**";
    private static final String OUTRIGHTS_ODDS = "/app/outrights/odds/**";

    /**
     * 该方法用于注册拦截器
     * 可注册多个拦截器，多个拦截器组成一个拦截器链
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(requestInterceptor())
                .addPathPatterns(ALL)
                .excludePathPatterns("/app/entrance/control/service");
        registry.addInterceptor(loginInterceptor())
                .addPathPatterns(USER,"/app/account/**",ORDER, LEAGUE,ODDS,OUTRIGHTS_ODDS)
                .excludePathPatterns("/app/user/file/**");
    }

    @Override
    public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
        SelfHttpMessageConverter fastConverter = new SelfHttpMessageConverter();
        fastConverter.setFastJsonConfig(JsonConfig.getFastJsonConfig());
        converters.add(fastConverter);
    }

    @Override
    public void extendMessageConverters(List<HttpMessageConverter<?>> converters) {
        List<HttpMessageConverter<?>> tmp = new ArrayList<>(converters);
        converters.clear();
        tmp.forEach(o -> {
            // 去掉jackson序列化
            if (o instanceof AbstractJackson2HttpMessageConverter) {
                // skip
            } else {
                converters.add(o);
            }
        });
    }

    @Bean
    public RequestInterceptor requestInterceptor() {
        return new RequestInterceptor();
    }

    @Bean
    public LoginInterceptor loginInterceptor() {
        return new LoginInterceptor();
    }
}
