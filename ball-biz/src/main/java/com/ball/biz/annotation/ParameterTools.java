package com.ball.biz.annotation;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.LocalVariableTableParameterNameDiscoverer;
import org.springframework.core.ParameterNameDiscoverer;
import org.springframework.expression.BeanResolver;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

/**
 * @author lhl
 * @date 2022/10/27 下午6:14
 */
@Slf4j
@Component
public class ParameterTools {

    private ParameterNameDiscoverer discoverer = new LocalVariableTableParameterNameDiscoverer();
    @Autowired
    private ExpressionParser parser;
    @Autowired
    private BeanResolver beanResolver;


    public String parseKey(String key, Method method, Object[] args) {
        String[] paraNameArr = discoverer.getParameterNames(method);
        StandardEvaluationContext context = new StandardEvaluationContext();
        context.setBeanResolver(beanResolver);
        for (int i = 0; i < paraNameArr.length; i++) {
            context.setVariable(paraNameArr[i], args[i]);
        }
        return parser.parseExpression(key).getValue(context, String.class);
    }
}
