package com.ball.common.service;

import com.alibaba.fastjson.JSONObject;
import com.ball.base.exception.AssertException;
import com.ball.base.exception.BizErr;
import com.ball.base.util.BizAssert;
import com.ball.common.bo.EmailGunResponse;
import com.ball.common.exception.CommonErrorCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

/**
 * @author littlehow
 */
@Slf4j
@Service
public class EmailGunService implements InitializingBean {

    @Value("${message.mailgun.url:https://api.mailgun.net/v3/jc853.com/messages}")
    private String apiUrl;
    @Value("${message.mailgun.user:api}")
    private String userName;
    @Value("${message.mailgun.password:xxxxx}")
    private String password;
    @Value("${message.mailgun.fromUser:noreplay@xxxxx.com}")
    private String fromUser;

    private RestTemplate restTemplate;

    public void send(String email, String subject, String content) {
        try {
            // 构建参数map
            MultiValueMap<String, String> data = new LinkedMultiValueMap<>();
            data.add("from", fromUser);
            data.add("to", email);
            data.add("subject", subject);
            data.add("text", content);
            log.info("request url={}, param={}", apiUrl,  data);
            String responseString = restTemplate.postForObject(apiUrl, request(email, subject, content), String.class);
            log.info("request url={}, response={}", apiUrl, responseString);
            EmailGunResponse response = JSONObject.parseObject(responseString, EmailGunResponse.class);
            BizAssert.notNull(response, CommonErrorCode.EMAIL_SEND_FAIL);
            BizAssert.isTrue(response.success(), CommonErrorCode.EMAIL_SEND_FAIL);
            log.info("sending message succeeded. email is {}", email);
        } catch (AssertException e) {
            throw e;
        } catch (Exception e) {
            log.warn("send email Message {}:sending message fail", email, e);
            throw new BizErr(CommonErrorCode.EMAIL_SEND_FAIL);
        }


    }

    private HttpEntity<MultiValueMap<String, String>> request(String email, String subject, String content) {
        // 构建参数map
        MultiValueMap<String, String> data = new LinkedMultiValueMap<>();
        data.add("from", fromUser);
        data.add("to", email);
        data.add("subject", subject);
        data.add("text", content);

        //设置请求头(注意会产生中文乱码)
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);
        return new HttpEntity<>(data, headers);
    }

    @Override
    public void afterPropertiesSet() {
        restTemplate = new RestTemplateBuilder()
                .basicAuthentication(userName, password).build();
    }

    public static void main(String[] args) {
        EmailGunService service = new EmailGunService();
        service.apiUrl = "https://api.mailgun.net/v3/jc853.com/messages";
        service.userName = "api";
        service.password = "xxxxx";
        service.fromUser = "noreplay@xxxxx.com";
        service.afterPropertiesSet();
        service.send("...", "【XXXXX】驗證碼通知", "您的驗證碼為：888888，10分鐘內有效，請勿泄露。");
    }
}
