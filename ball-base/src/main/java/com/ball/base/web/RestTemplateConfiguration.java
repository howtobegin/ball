package com.ball.base.web;

import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

/**
 * @author littlehow
 */
@Configuration
public class RestTemplateConfiguration {

    private static final int HTTP_CLIENT_MAX_TOTAL = 200;
    private static final int HTTP_CLIENT_MAX_PER_ROUTE = 100;

    @Value("${rest.template.socket.timeout:10000}")
    private int requestSocketTimeOut = 10000;

    @Value("${rest.template.connect.timeout:3000}")
    private int requestConnectTimeout = 3000;

    @Value("${rest.template.connection.request.timeout:10000}")
    private int requestConnectionRequestTimeout = 10000;

    @Value("${http.retry.count:3}")
    private int retryCount;

    @Bean("selfRestTemplate")
    public RestTemplate restTemplate() {
        return new RestTemplate(httpRequestFactory());
    }
    @Bean
    public ClientHttpRequestFactory httpRequestFactory() {
        return new HttpComponentsClientHttpRequestFactory(httpClient());
    }

    @Bean
    public HttpClient httpClient() {
        Registry<ConnectionSocketFactory> registry = RegistryBuilder.<ConnectionSocketFactory>create()
                .register("http", PlainConnectionSocketFactory.getSocketFactory())
                .register("https", SSLConnectionSocketFactory.getSocketFactory())
                .build();
        // 不使用连接池 update littlehow 2021-04-18
        //BasicHttpClientConnectionManager clientConnectionManager = new BasicHttpClientConnectionManager();
        //clientConnectionManager.setConnectionConfig(ConnectionConfig.DEFAULT);
        PoolingHttpClientConnectionManager connectionManager = new PoolingHttpClientConnectionManager(registry);
        connectionManager.setMaxTotal(HTTP_CLIENT_MAX_TOTAL);
        connectionManager.setDefaultMaxPerRoute(HTTP_CLIENT_MAX_PER_ROUTE);
        RequestConfig requestConfig = RequestConfig.custom()
                .setSocketTimeout(requestSocketTimeOut)
                .setConnectTimeout(requestConnectTimeout)
                .setConnectionRequestTimeout(requestConnectionRequestTimeout)
                .build();
        return HttpClientBuilder.create()
                .setDefaultRequestConfig(requestConfig)
                .setConnectionManager(connectionManager)
                // 统一重试次数 默认为3
                .setRetryHandler((e, count, httpContext) -> count <= retryCount)
                .build();
    }
}
