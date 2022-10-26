package com.ball.common.test.service;

import com.ball.base.model.enums.YesOrNo;
import com.ball.base.util.AesPeculiarUtil;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

/**
 * @author JimChery
 */
public class ServiceControl {
    private static String url = "http://localhost:port/app/entrance/control/service";
    private static RestTemplate restTemplate = new RestTemplate();

    public static void main(String[] args) {
        String result = restTemplate.postForObject(url, buildEntity(YesOrNo.YES.v), String.class);
        System.out.println(result);
    }

    private static HttpEntity buildEntity(Integer operation) {
        HttpHeaders headers = new HttpHeaders();
        // 设置content-type
        headers.setContentType(MediaType.APPLICATION_JSON);
        Map<String, Object> param = new HashMap<>();
        String time = "" + System.currentTimeMillis();
        String code = AesPeculiarUtil.encode("fbi-" + time, "svr");
        param.put("code", code);
        param.put("time", time);
        param.put("operation", operation);
        return new HttpEntity<>(param, headers);
    }
}
