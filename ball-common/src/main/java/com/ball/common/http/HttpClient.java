package com.ball.common.http;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.ball.base.exception.BizErr;
import com.ball.common.exception.CommonErrorCode;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.*;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.AuthCache;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.auth.BasicScheme;
import org.apache.http.impl.client.BasicAuthCache;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.springframework.util.StreamUtils;

import java.io.IOException;
import java.util.Map;

/**
 * Http请求工具类
 *
 * @date 2018/5/30
 */
@Slf4j
public class HttpClient {

    static final String JsonMime = "application/json";
    static final String APP_ID = "appId";

    private org.apache.http.client.HttpClient httpClient;
    private HttpBaseConfig config;
    /**
     * 获取调用api发送短信和邮件的超时时间
     */
   public static int maxApiMessageConnectionTimeOut = 10000;

    public HttpClient(HttpBaseConfig config) {
        httpClient = HttpConnectManager.getHttpClient();
        this.config = config;
    }


    /**
     * @param httpRequest 发送http的request
     * @return String 返回的信息
     * @Name: sendRequest
     * @Description: 发送http请求
     * @Version: V0.1.0
     */
    private String sendRequest(HttpRequestBase httpRequest) {

        httpRequest.addHeader(HttpHeaders.CONTENT_TYPE, JsonMime);
        httpRequest.addHeader(APP_ID, HttpHeaderConstant.SOURCE_ID);
        httpRequest.addHeader(HttpHeaderConstant.REQUESTTIMESTAMP, String.valueOf(System.currentTimeMillis()));

        String res = "";
        HttpResponse response;
        HttpEntity entity = null;
        try {
            response = httpClient.execute(httpRequest);
            entity = response.getEntity();

            log.info("=======Http Response=========Http_Status:[{}]", response.getStatusLine().getStatusCode());

            if (log.isDebugEnabled()) {
                log.debug("Http Response status : " + response.getStatusLine());
                log.debug("Http Response entity : " + entity);
            }
            if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                res = entity != null ? EntityUtils.toString(entity, "utf-8") : "";

                log.info("=======Http Response=========Response body:{}", res);

            } else {
                log.error("HttpStatus is not 200.");
                throw new BizErr(CommonErrorCode.HTTP_CLIENT_REQUEST_FAIL);
            }
        } catch (IOException e) {
            log.error("sendRequest error:" + e.getMessage(), e);
            throw new BizErr(CommonErrorCode.HTTP_CLIENT_REQUEST_FAIL);
        } finally {
            try {
                if (entity != null) {
                    EntityUtils.consume(entity);
                }
                httpRequest.releaseConnection();
            } catch (IOException e) {
                log.error("EntityUtils.consume error:" + e.getMessage(), e);
            }
        }

        return res;
    }


    /**
     * 带有身份验证的post请求调用，
     *
     * @param uri      api请求地址
     * @param data     参数map
     * @param userName 凭证用户名
     * @param password 凭证密码
     * @throws Exception
     */
    public static JSONObject postWithAuth(String uri, Map<String, Object> data,
                                          String userName, String password) throws Exception {
        // 获取调用api发送短信和邮件的超时时间(毫秒)
        int timeOut = maxApiMessageConnectionTimeOut;
        CloseableHttpClient defaultClient = null;
        try {
            // 设置超时时间
            RequestConfig defaultRequestConfig = RequestConfig.custom()
                    .setConnectTimeout(timeOut)
                    .build();

            // 创建默认的client
            defaultClient = HttpClients.custom()
                    .setDefaultRequestConfig(defaultRequestConfig)
                    .build();

            /*========== 添加http认证信息 ========== 开始*/
            log.info("HttpsClient[postWithAuth] : add credential info to context");
            // 新建凭证注册器
            CredentialsProvider credentialsProvider = new BasicCredentialsProvider();
            // 注册
            credentialsProvider.setCredentials(
                    AuthScope.ANY,
                    new UsernamePasswordCredentials(userName, password));

            /**
             * https://stackoverflow.com/questions/20914311/httpclientbuilder-basic-auth
             * From the Preemptive Authentication Documentation here:
             *
             * http://hc.apache.org/httpcomponents-client-ga/tutorial/html/authentication.html
             *
             * By default, httpclient will not provide credentials preemptively, it will first create a HTTP request without authentication parameters. This is by design, as a security precaution, and as part of the spec. But, this causes issues if you don't retry the connection, or wherever you're connecting to expects you to send authentication details on the first connection. It also causes extra latency to a request, as you need to make multiple calls, and causes 401s to appear in the logs.
             *
             * The workaround is to use an authentication cache to pretend that you've already connected to the server once. This means you'll only make one HTTP call and won't see a 401 in the logs:
             */
            // Create AuthCache instance
            AuthCache authCache = new BasicAuthCache();
            // Generate BASIC scheme object and add it to the local auth cache
            BasicScheme basicAuth = new BasicScheme();

            // post请求对象
            HttpPost post = new HttpPost(uri);
            HttpHost targetHost = new HttpHost(post.getURI().getHost(), post.getURI().getPort());

            authCache.put(targetHost, basicAuth);

            // 新建HttpClient上下文
            HttpClientContext context = HttpClientContext.create();
            // 设置凭证注册器
            context.setCredentialsProvider(credentialsProvider);
            context.setAuthCache(authCache);
            log.info("HttpsClient[postWithAuth] : add credential info to context success");
            /*========== 添加http认证信息 ========== 结束*/

            //创建请求体
            StringEntity requestEntity = new StringEntity(JSON.toJSONString(data), ContentType.APPLICATION_JSON);
            // 设置请求参数
            post.setEntity(requestEntity);
            log.info("HttpsClient[postWithAuth] : set param entity success");

            //执行请求
            log.info("HttpsClient[postWithAuth] : execute post method");
            // 请求并获取返回对象
            CloseableHttpResponse response = defaultClient.execute(post, context);
            // 获取返回的对象
            HttpEntity entity = response.getEntity();

            // 判断返回结果
            if (entity == null) {
                // 响应体为空
                log.error("HttpClient[postWithAuth] : send message fail,response entity is null");
                throw new Exception("HttpClient[postWithAuth] : send message fail,response entity is null");
            }

            // 判断返回结果,响应体为null或返回信息不为success,则为发送失败
            JSONObject result = JSON.parseObject(
                    new String(StreamUtils.copyToByteArray(entity.getContent()), "utf-8"));

            // 关闭请求
            defaultClient.close();
            log.info("HttpClient[postWithAuth] : send message success,toUser=[{}]", data.get("to_user"));

            return result;

        } catch (Exception e) {
            log.error("HttpClient[postWithAuth] : execute post method error", e);
            throw e;
        } finally {
            try {
                // 关闭请求
                if (defaultClient != null) {
                    defaultClient.close();
                }
            } catch (IOException e) {
                log.error("HttpClient[postWithAuth] : close client error", e);
            }
        }
    }
}
