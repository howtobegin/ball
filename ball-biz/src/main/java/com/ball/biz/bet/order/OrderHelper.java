package com.ball.biz.bet.order;

import com.alibaba.fastjson.JSON;
import com.ball.base.exception.BizException;
import org.springframework.util.StringUtils;

/**
 * @author lhl
 * @date 2022/10/21 下午5:11
 */
public class OrderHelper {
    public static <T> T parse(String oddsData, Class<T> clazz) {
        try {
            return !StringUtils.isEmpty(oddsData) ? JSON.parseObject(oddsData, clazz) : clazz.newInstance();
        } catch (Exception e) {
            throw new BizException("oddsData解析出错");
        }
    }
}
