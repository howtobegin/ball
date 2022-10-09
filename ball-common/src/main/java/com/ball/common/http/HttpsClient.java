package com.ball.common.http;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.io.IOException;
import java.nio.charset.Charset;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 *
 */
@Slf4j
public class HttpsClient {

    private HttpClient client;

    private RequestConfig requestConfig;

    private static final int CONNECTION_REQUEST_TIMEOUT = 5 * 1000; //从连接池中获取连接的超时时间
    private static final int CONNECTION_TIMEOUT = 5 * 1000; //与服务器连接超时时间
    private static final int SOCKET_TIMEOUT = 5 * 1000; //socket读数据超时时间


    public HttpsClient() {

        X509TrustManager xtm = new X509TrustManager() {
            public void checkClientTrusted(X509Certificate[] arg0, String arg1)
                    throws CertificateException {
            }

            public void checkServerTrusted(X509Certificate[] arg0, String arg1)
                    throws CertificateException {
            }

            public X509Certificate[] getAcceptedIssuers() {
                return new X509Certificate[]{};
            }
        };

        try {
            SSLContext context = SSLContext.getInstance("TLS");

            context.init(null, new TrustManager[]{xtm}, null);
            SSLConnectionSocketFactory scsf = new SSLConnectionSocketFactory(context, NoopHostnameVerifier.INSTANCE);
            Registry<ConnectionSocketFactory> sfr = RegistryBuilder.<ConnectionSocketFactory>create()
                    .register("http", PlainConnectionSocketFactory.INSTANCE)
                    .register("https", scsf).build();
            PoolingHttpClientConnectionManager pcm = new PoolingHttpClientConnectionManager(sfr);

            requestConfig = RequestConfig.custom()
                    .setConnectionRequestTimeout(CONNECTION_REQUEST_TIMEOUT)
                    .setConnectTimeout(CONNECTION_TIMEOUT)
                    .setSocketTimeout(SOCKET_TIMEOUT).build();

            client = HttpClientBuilder.create().setConnectionManager(pcm).setDefaultRequestConfig(requestConfig).build();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public JSONObject get(String url, Map<String, String> params) throws IOException {
        List<NameValuePair> urlParameters = new ArrayList<NameValuePair>();
        for (Map.Entry<String, String> entry : params.entrySet()) {
            urlParameters.add(new BasicNameValuePair(entry.getKey(), entry.getValue()));
        }

        String paramUrl = URLEncodedUtils.format(urlParameters, Charset.forName("UTF-8"));
        HttpGet request = new HttpGet(url + "?" + paramUrl);

        return send(request);
    }


    public JSONObject post(String url, Map<String, String> params) throws IOException {
        HttpPost request = new HttpPost(url);
        List<NameValuePair> urlParameters = new ArrayList<NameValuePair>();
        for (Map.Entry<String, String> entry : params.entrySet()) {
            urlParameters.add(new BasicNameValuePair(entry.getKey(), entry.getValue()));
        }

        HttpEntity postParams = new UrlEncodedFormEntity(urlParameters);
        request.setEntity(postParams);

        return send(request);
    }


    /**
     * 使用StringEntity提交数据
     * @param url
     * @param params
     * @return
     * @throws IOException
     * add by cuiyong 2018.09.06
     */
    public JSONObject postStringEntity(String url, Map<String, String> params)throws IOException {
        HttpPost request = new HttpPost(url);
        JSONObject object = new JSONObject();
        //遍历生成JSON串
        for (Map.Entry<String, String> entry : params.entrySet()) {
            object.put(entry.getKey(),entry.getValue());
        }
        StringEntity se = new StringEntity(object.toJSONString(), "utf-8");
        request.setEntity(se);
        request.addHeader("Content-Type","application/json");

        return send(request);
    }



    private JSONObject send(HttpRequestBase request) throws IOException {
        JSONObject jsonResult = null;
        try {
            request.addHeader("Connection", "close");
            HttpResponse response = client.execute(request);
            if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                String strResult = EntityUtils.toString(response.getEntity());
                jsonResult = JSON.parseObject(strResult);
            }
            return jsonResult;
        } catch (IOException e) {
            log.warn("HTTP send error: {}", e.getMessage());
            throw e;
        } finally {
            if (request != null) {
                request.releaseConnection();
            }
        }
    }
}
