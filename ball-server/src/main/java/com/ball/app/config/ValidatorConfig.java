package com.ball.app.config;

import org.hibernate.validator.messageinterpolation.ResourceBundleMessageInterpolator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;
import org.springframework.validation.beanvalidation.MessageSourceResourceBundleLocator;

import javax.validation.Validator;


/**
 * Validator的国际化配置.
 *
 */
@Configuration
public class ValidatorConfig {

    @Autowired
    private MessageSource messageSource;

    @Bean
    public Validator validator(){
        LocalValidatorFactoryBean validator = new LocalValidatorFactoryBean();
        MessageSourceResourceBundleLocator locator = new MessageSourceResourceBundleLocator(messageSource);
        validator.setMessageInterpolator(new ResourceBundleMessageInterpolator(locator));
        return validator;
    }
}
