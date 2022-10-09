package com.ball.common.http;

import com.alibaba.fastjson.JSON;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Service
@Slf4j
@RequiredArgsConstructor
public class HttpService {

    private final RestTemplate restTemplate;

    public <T> Object basePost(String url, Object params, Class<T> clazz) {
        String result = getRpcResult(url, buildBaseHttpEntity(JSON.parseObject(JSON.toJSONString(params)), HttpMethod.POST, url), HttpMethod.POST);
        if (clazz == null) {
            return result;
        }
        return JSON.parseObject(result, clazz);
    }

    private HttpEntity<Map<String, Object>> buildBaseHttpEntity(Map<String, Object> params, HttpMethod method, String path) {
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
        return new HttpEntity<>(params, headers);
    }

    private String getRpcResult(String url, HttpEntity<Map<String, Object>> params, HttpMethod method) {
        //第一次不算重试次数
        for (int i = 0; i < 3; i++) {
            try {
                ResponseEntity<String> entity = restTemplate.exchange(url, method, params, String.class, params.getBody());
                log.info("method：{}, url:{}, params:{}, response status:{}", method, url, params, entity.getStatusCode());
                if (entity.getStatusCode().equals(HttpStatus.OK)) {
                    return entity.getBody();
                }
            } catch (Exception e) {
                log.error("method：{}, url:{}, params:{}" + e.getMessage(), method, url, params, e);
            }
        }
        log.error("getRpcResult failed ,url:{},params:{}", url, params);
        return null;
    }
}
