package com.ball.common.service;

import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Maps;
import com.ball.base.exception.BizErr;
import com.ball.base.util.BizAssert;
import com.ball.base.util.SimpleCodeUtil;
import com.ball.common.bo.SmsResponse;
import com.ball.common.exception.CommonErrorCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

/**
 * @author littlehow
 */
@Service
@Slf4j
public class SmsPaasooService {
    @Autowired
    private RestTemplate restTemplate;

    @Value("${message.sms.paasoo.url:https://api.paasoo.com/json}")
    private String apiUrl;
    @Value("${message.sms.paasoo.user:xxx}")
    private String userName;
    @Value("${message.sms.paasoo.password:xxx}")
    private String password;
    @Value("${message.sms.paasoo.from:xxx}")
    private String from;
    @Value("${message.sms.content.max:1000}")
    private int maxContentLen;
    @Value("${message.sms.cly.sign:xxx}")
    private String sign;

    public void send(String phone, String content) {
        content = "【" + sign + "】" + content;
        log.info(content);
        if (content.getBytes().length <= maxContentLen) {
            log.info("SmsUtil[send] : start sending message,mobile=[{}]", phone);
            try {
                // 构建参数map
                Map<String, String> data = Maps.newHashMap();
                data.put("key", userName);
                data.put("secret", password);
                data.put("from", from);
                data.put("to", phone);
                data.put("text", content);
//                List<NameValuePair> params = new ArrayList<>();
//                data.forEach((key, value) ->
//                    params.add(new BasicNameValuePair(key, value))
//                );
//                String paramString = EntityUtils.toString(new UrlEncodedFormEntity(params, "UTF-8"));
                StringBuilder sb = new StringBuilder();
                data.forEach((key, value) ->
                    sb.append(key).append("=").append(value).append("&")
                );
                String params = sb.substring(0, sb.length() - 1);
                log.info("request url={}, param={}", apiUrl,  params);
                String responseString = restTemplate.getForObject(apiUrl + "?" + params, String.class);
                log.info("request url={}, response={}", apiUrl, responseString);
                SmsResponse response = JSONObject.parseObject(responseString, SmsResponse.class);
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

    public static void main(String[] args) {
        SmsPaasooService smsPaasooService = new SmsPaasooService();
        smsPaasooService.apiUrl = "https://api.paasoo.com/json";
        smsPaasooService.from = "xxx";
        smsPaasooService.maxContentLen = 1000;
        smsPaasooService.password = "xxx";
        smsPaasooService.restTemplate = new RestTemplate();
        smsPaasooService.sign = "xxx";
        smsPaasooService.userName = "xxx";
        for (int i = 0; i < 1; i++) {
            smsPaasooService.send("+85259722167", "Verification code " + SimpleCodeUtil.getVerifyCode(6));
        }

    }
}
