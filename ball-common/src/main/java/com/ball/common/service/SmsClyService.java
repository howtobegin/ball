package com.ball.common.service;

import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Maps;
import com.ball.base.exception.BizErr;
import com.ball.base.util.BizAssert;
import com.ball.base.util.EncryptUtil;
import com.ball.common.bo.SmsClyResponse;
import com.ball.common.exception.CommonErrorCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

/**
 * @author JimChery
 */
//@Service
@Slf4j
public class SmsClyService {
    @Autowired
    private RestTemplate restTemplate;

    @Value("${message.sms.cly.url:https://www.sms-cly.cn/v7/msg/submit.json}")
    private String apiUrl;
    @Value("${message.sms.cly.user:}")
    private String userName;
    @Value("${message.sms.cly.password:}")
    private String password;
    @Value("${message.sms.content.max:1000}")
    private int maxContentLen;
    @Value("${message.sms.cly.sign:}")
    private String sign;

    public void send(String phone, String content) {
        content = "【" + sign + "】" + content;
        if (content.getBytes().length <= maxContentLen) {
            log.info("SmsUtil[send] : start sending message,mobile=[{}]", phone);
            try {
                // 构建参数map
                Map<String, String> data = Maps.newHashMap();
                data.put("userName", userName);
                data.put("sign", EncryptUtil.md5(userName + password + phone + content));
                data.put("mobile", phone);
                data.put("content", content);
                log.info("request url={}, param={}", apiUrl,  data);
                String responseString = restTemplate.postForObject(apiUrl, buildEntity(data), String.class);
                log.info("request url={}, response={}", apiUrl, responseString);
                SmsClyResponse response = JSONObject.parseObject(responseString, SmsClyResponse.class);
                BizAssert.notNull(response, CommonErrorCode.SMS_SEND_FAIL);
                BizAssert.isTrue(response.success(), CommonErrorCode.SMS_SEND_FAIL);
                log.info("sending message succeeded. phone is {}", phone);
            } catch (Exception exception) {
                log.warn("sendMessage {}:sending message fail", phone, exception);
                throw new BizErr(CommonErrorCode.SMS_SEND_FAIL);
            }
        } else {
            log.error("sendMessage {}: length of sms message is too long", phone);
            throw new BizErr(CommonErrorCode.SMS_SEND_FAIL);
        }
    }

    private static HttpEntity buildEntity(Map<String, String> param) {
        HttpHeaders headers = new HttpHeaders();
        // 设置content-type
        headers.setContentType(MediaType.APPLICATION_JSON);
        return new HttpEntity<>(param, headers);
    }
}
