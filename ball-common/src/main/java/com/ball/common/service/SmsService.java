package com.ball.common.service;

import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Maps;
import com.ball.base.exception.BizErr;
import com.ball.base.util.BizAssert;
import com.ball.common.exception.CommonErrorCode;
import com.ball.common.http.HttpClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;

import java.util.Map;

/**
 * @author JimChery
 */
//@Service
@Slf4j
public class SmsService {

    @Value("${message.sms.url}")
    private String apiUrl;
    @Value("${message.sms.user}")
    private String userName;
    @Value("${message.sms.password}")
    private String password;
    @Value("${message.sms.content.max}")
    private int maxContentLen;
    @Value("${message.sms.mock:false}")
    private String mock;

    public void send(String phone, String content) {

        if (content.getBytes().length <= maxContentLen) {
            log.info("SmsUtil[send] : start sending message,mobile=[{}]", phone);

            try {
                // 构建参数map
                Map<String, Object> data = Maps.newHashMap();
                data.put("mock", mock);
                data.put("to", phone);
                data.put("content", content);
                BizAssert.notNull(apiUrl, CommonErrorCode.SMS_CONFIG_ERROR);
                BizAssert.notNull(userName, CommonErrorCode.SMS_CONFIG_ERROR);
                BizAssert.notNull(password, CommonErrorCode.SMS_CONFIG_ERROR);
                // 调用http请求的api
                JSONObject result = HttpClient.postWithAuth(apiUrl, data, userName, password);
                log.info("send result {}", result);
                BizAssert.notNull(result, CommonErrorCode.SMS_SEND_FAIL);
                BizAssert.notNull(result.get("sms_id"), CommonErrorCode.SMS_SEND_FAIL);
                log.info("SmsUtil[send] : sending message succeeded. phone is {}", phone);
            } catch (Exception exception) {
                log.warn("sendMessage {}:sending message fail", phone, exception);
                throw new BizErr(CommonErrorCode.SMS_SEND_FAIL);
            }
        } else {
            log.error("sendMessage {}: length of sms message is too long", phone);
            throw new BizErr(CommonErrorCode.SMS_SEND_FAIL);
        }
    }
}
