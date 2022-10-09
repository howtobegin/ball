package com.ball.boss.config;

import com.ball.base.web.SelfHttpMessageConverter;
import com.ball.base.web.JsonConfig;
import com.ball.boss.interceptor.LoginInterceptor;
import com.ball.boss.interceptor.RequestInterceptor;
import com.ball.boss.interceptor.UserAuthInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.AbstractJackson2HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.ArrayList;
import java.util.List;

@Configuration
public class InterceptorConfig implements WebMvcConfigurer {

    /**
     * 该方法用于注册拦截器
     * 可注册多个拦截器，多个拦截器组成一个拦截器链
     */
    private static final String BOSS_ALL = "/boss/**";
    private static final String APP_ALL = "/app/**";

    /**
     * 该方法用于注册拦截器
     * 可注册多个拦截器，多个拦截器组成一个拦截器链
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(requestInterceptor())
                .addPathPatterns(BOSS_ALL, APP_ALL);
        registry.addInterceptor(loginInterceptor())
                .addPathPatterns(BOSS_ALL, APP_ALL)
                .excludePathPatterns("/boss/account/login",
                        "/boss/account/sendCode", "/boss/account/logout", "/boss/file/**", "/boss/add/user/hotspot");
        registry.addInterceptor(userAuthInterceptor())
                .addPathPatterns(BOSS_ALL, APP_ALL)
                .excludePathPatterns(
                        "/boss/common/**",
                        "/boss/account/login",
                        "/boss/account/sendCode",
                        "/boss/account/logout",
                        "/boss/system/dictionary/getAll",
                        "/boss/system/user/getRoleMenu",
                        "/boss/system/user/authorize",
                        "/boss/system/user/changePassword",
                        "/boss/account/checkKickOut",
                        "/boss/file/**",
                        "/boss/add/user/hotspot"
                );
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

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**");
    }


    @Bean
    public UserAuthInterceptor userAuthInterceptor() {
        return new UserAuthInterceptor();
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
