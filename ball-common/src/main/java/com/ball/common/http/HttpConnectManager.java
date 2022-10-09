package com.ball.common.http;

import org.apache.http.NoHttpResponseException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.TrustSelfSignedStrategy;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.ssl.SSLContexts;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.net.ssl.SSLContext;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.util.concurrent.TimeUnit;


public class HttpConnectManager {
    private static Logger log = LoggerFactory.getLogger(HttpConnectManager.class);
    private static PoolingHttpClientConnectionManager cm;

    /**
     * 最大连接数
     */
    public final static int MAX_TOTAL_CONNECTIONS = 500;
    /**
     * 获取连接的最大等待时间
     */
    public final static int WAIT_TIMEOUT = 6000;
    /**
     * 每个路由最大连接数
     */
    public final static int MAX_ROUTE_CONNECTIONS = 300;
    /**
     * 连接超时时间
     */
    public final static int CONNECT_TIMEOUT = 3000;
    /**
     * 读取超时时间
     */
    public final static int READ_TIMEOUT = 3000;

    static {
        SSLContext sslcontext = null;
        try {
            sslcontext = SSLContexts.custom().loadTrustMaterial(null,
                    new TrustSelfSignedStrategy())
                    .build();
        } catch (KeyManagementException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (KeyStoreException e) {
            e.printStackTrace();
        }
        SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(
                sslcontext);
        Registry<ConnectionSocketFactory> socketFactoryRegistry = RegistryBuilder.<ConnectionSocketFactory>create()
                .register("http", PlainConnectionSocketFactory.getSocketFactory())
                .register("https", sslsf)
                .build();

        cm = new PoolingHttpClientConnectionManager(socketFactoryRegistry);
        cm.setMaxTotal(MAX_TOTAL_CONNECTIONS);

        cm.setDefaultMaxPerRoute(MAX_ROUTE_CONNECTIONS);

    }

    /**
     * @return CloseableHttpClient 新建立的httpclient
     * @Name: getHttpClient
     * @Description: 生成一个httpClient
     */
    public static CloseableHttpClient getHttpClient() {
        //设置默认时间
        RequestConfig params = RequestConfig.custom()
                .setConnectTimeout(10000)
                .setConnectionRequestTimeout(10000)
                .setSocketTimeout(10000)
                .setExpectContinueEnabled(true).build();


        return HttpClientBuilder.create()
                .setDefaultRequestConfig(params)
                .setConnectionManager(cm)
                .setConnectionTimeToLive(10, TimeUnit.SECONDS)
                .evictIdleConnections(10L, TimeUnit.SECONDS)
                .setRetryHandler((exception, executionCount, context) -> {
                    if (executionCount > 3) {
                        log.warn("Maximum tries reached for client http pool ");
                        return false;
                    }

                    if (exception instanceof NoHttpResponseException) {
                        log.warn("NoHttpResponseException on " + executionCount + " call");
                        return true;
                    }

                    if (exception instanceof ClientProtocolException) {
                        log.warn("ClientProtocolException on " + executionCount + " call");
                        return true;
                    }
                    return false;
                })
                .build();
    }

    /**
     * @Name: release
     * @Description: 释放连接池的所有链接
     */
    public static void release() {
        if (cm != null) {
            cm.shutdown();
        }
    }
}
