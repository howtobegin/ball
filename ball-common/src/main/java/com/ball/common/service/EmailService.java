package com.ball.common.service;

import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Maps;
import com.ball.base.exception.BizErr;
import com.ball.base.util.BizAssert;
import com.ball.common.exception.CommonErrorCode;
import com.ball.common.http.HttpClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

import static com.google.common.collect.Lists.newArrayList;

/**
 * @author littlehow
 */
@Slf4j
@Service
public class EmailService {
    @Value("${message.email.url:}")
    private String apiUrl;
    @Value("${message.email.user:}")
    private String userName;
    @Value("${message.email.password:}")
    private String password;
    @Value("${message.email.fromUser:}")
    private String fromUser;

    public void send(String email, String subject, String content) {
        // 获取调用发送邮件的api接口地址
        BizAssert.notNull(apiUrl, CommonErrorCode.EMAIL_CONFIG_ERROR);
        BizAssert.notNull(userName, CommonErrorCode.EMAIL_CONFIG_ERROR);
        BizAssert.notNull(password, CommonErrorCode.EMAIL_CONFIG_ERROR);
        BizAssert.notNull(fromUser, CommonErrorCode.EMAIL_CONFIG_ERROR);

        List<String> toList = newArrayList(email);
        // 构建参数
        Map<String, Object> data = Maps.newHashMap();
        data.put("from_user", fromUser);
        data.put("from_name", fromUser);
        data.put("subject", subject);
        data.put("content", content);
        data.put("to", toList);

        try {
            // 调用api
            JSONObject result = HttpClient.postWithAuth(apiUrl, data, userName, password);
            if (result == null) {
                log.warn("EmailUtil[postApi] : send message fail,response entity is null");
                throw new Exception("EmailUtil[postApi] : send message fail,response entity is null");
            }
            /**
             * 邮件和短信改到一半，理想情况下想要只判断返回码就是200和201即可，所以删除email_id的判断
             */
            log.info("send email result:{}", result);

        } catch (Exception e) {
            log.error("send http error", e);
            throw new BizErr(CommonErrorCode.HTTP_CLIENT_REQUEST_FAIL);
        }

    }
}
