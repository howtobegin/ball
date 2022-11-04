package com.ball.app.interceptor;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.Ordered;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.ContentCachingRequestWrapper;
import org.springframework.web.util.ContentCachingResponseWrapper;
import org.springframework.web.util.WebUtils;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.*;

/**
 * ref:https://juejin.im/post/5b86148e6fb9a01a02312216
 */
@Slf4j
@Component
public class HttpTraceLogFilter extends OncePerRequestFilter implements Ordered {

    /**
     * 头尾显示多少个字符，默认头尾显示两个字符
     */
    @Value("${show.desen.char.number:2}")
    private Integer showDesenCharNumber;

    /******************************************************************************************************************
     ***********************************************打印配置相关开始 ****************************************************
     */

    /**
     * 如果uri是如下列表中的一项开头的，那么request和response都不打印
     * 比如健康检查、swagger
     */
    private List<String> ignorePrintUriList = Lists.newArrayList("/health", "/swagger-ui", "/favicon",
            "/swagger-resources", "/doc.html", "/v2/api-docs", "/webjars");


    private Set<String> HEADERS = Sets.newHashSet("LANG", "Ball-TOKEN", "B-TOKEN");
    /**
     * 如果uri是如下列表中的一项开头的，那么request不打印
     * 比如文件上传相关的，那就request不打印，但是打印response
     */
    private List<String> ignorePrintRequestUriList = Lists.newArrayList( "/app/user/file","/app/file");
    /**
     * 如果uri是如下列表中的一项开头的，那么response不打印
     * 比如文章相关的，就是打印request不打印response，比如针对一些返回私钥的接口也需要配置在这里，不打印私钥信息
     */
    private List<String> ignorePrintResponseUriList = Lists.newArrayList("/app/article/", "/app/file/download",
            "/app/league/list", "/app/odds/list","/app/odds/match/one","/app/order/bet/current","/app/order/bet/history","/app/order/bet/history/date");

    /**
     * 需要脱敏的header头的key，在打印头的时候进行脱敏
     * 不区分大小写
     */
    private List<String> desenHeaderList = Lists.newArrayList("Ball-TOKEN", "TOKEN", "PKEY", "KEY");

    /******************************************************************************************************************
     ***********************************************打印配置相关结束 ****************************************************
     */


    class RequestData {
        Map<String, String> header;
        String request;
        String response;

        public RequestData() {
        }

        public Map<String, String> getHeader() {
            return header;
        }

        public void setHeader(Map<String, String> header) {
            this.header = header;
        }

        public String getRequest() {
            return request;
        }

        public void setRequest(String request) {
            this.request = request;
        }

        public String getResponse() {
            return response;
        }

        public void setResponse(String response) {
            this.response = response;
        }
    }

    @Override
    public int getOrder() {
        return Ordered.LOWEST_PRECEDENCE - 10;
    }


    /**
     * 如果uri开头是需要ignore的uri开头，那么不打印http trace
     * @param uri
     * @return
     */
    private boolean isIgnorePrint(String uri) {
        for (String item : ignorePrintUriList) {
            if (uri.startsWith(item)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 是否不打印request
     * @param uri
     * @return
     */
    private boolean isIgnorePrintRequest(String uri) {
        for (String item : ignorePrintRequestUriList) {
            if (uri.startsWith(item)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 是否不打印response
     * @param uri
     * @return
     */
    private boolean isIgnorePrintResponse(String uri) {
        for (String item : ignorePrintResponseUriList) {
            if (uri.startsWith(item)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 是否需要对内容进行脱敏
     * @param headerKey
     * @return
     */
    private boolean isDesenHeader(String headerKey) {
        for (String item : desenHeaderList) {
            if (headerKey.equalsIgnoreCase(item)) {
                return true;
            }
        }
        return false;
    }



    /**
     * @param request
     * @param response
     * @param filterChain
     * @throws ServletException
     * @throws IOException
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        if (!isRequestValid(request)) {
            filterChain.doFilter(request, response);
            return;
        }
        if (!(request instanceof ContentCachingRequestWrapper)) {
            request = new ContentCachingRequestWrapper(request);
        }
        if (!(response instanceof ContentCachingResponseWrapper)) {
            response = new ContentCachingResponseWrapper(response);
        }
        int status = HttpStatus.INTERNAL_SERVER_ERROR.value();
        long startTime = System.currentTimeMillis();
        try {
            filterChain.doFilter(request, response);
            status = response.getStatus();
        } finally {
            String path = request.getRequestURI();
            String IGNORE_CONTENT_TYPE = "multipart/form-data";

            HttpTraceLog traceLog = new HttpTraceLog();
            traceLog.setPath(path);
            traceLog.setMethod(request.getMethod());
            long latency = System.currentTimeMillis() - startTime;
            traceLog.setCostTime(latency);
            traceLog.setStatus(status);
            RequestData requestData = new RequestData();
            getRequestBody(request, requestData);
            getResponseBody(response, requestData);

            if (isIgnorePrint(path) || Objects.equals(IGNORE_CONTENT_TYPE, request.getContentType())) {
                updateResponse(response);
                return;
            }

            if (isIgnorePrintRequest(path)) {
                /**
                 * 输出post的request数据
                 */
                traceLog.setRequestBody("");
            } else {
                traceLog.setRequestBody(requestData.getRequest());
            }

            if (isIgnorePrintResponse(path)) {
                traceLog.setResponseBody("");
            } else {
                traceLog.setResponseBody(requestData.getResponse());
            }
            traceLog.setHeader(JSON.toJSONString(requestData.getHeader()));
            traceLog.setUserNo(String.valueOf(TraceLogContext.getUserNo()));
            traceLog.setIp(TraceLogContext.getIp());
            log.info("http trace log: {}", JSON.toJSONString(traceLog));
            updateResponse(response);
        }
    }

    private boolean isRequestValid(HttpServletRequest request) {
        try {
            new URI(request.getRequestURL().toString());
            return true;
        } catch (URISyntaxException ex) {
            return false;
        }
    }


    /**
     * 只留最开始2位和最末尾两位
     * @param data
     * @return
     *
     */
    private String desenData(String data) {
        int len = data.length();
        int totalShowDesenCharNumber = showDesenCharNumber * 2;
        if (len > totalShowDesenCharNumber) {
            StringBuilder sb = new StringBuilder(data);
            sb.replace(showDesenCharNumber, len-showDesenCharNumber, "****");
            return sb.toString();
        } else {
            return data;
        }
    }


    private Map<String, String> getHeaderInfo(HttpServletRequest request) {

        Map<String, String> map = new HashMap<>();
        /**
         * 打印关心的头部数据，在header中获取的时候，发现key都是小写了，所以这里需要关心的header key需要toLowerCase
         */
        Enumeration headerNames = request.getHeaderNames();
        while (headerNames.hasMoreElements()) {
            String key = (String) headerNames.nextElement();
            String value = request.getHeader(key);
            if (HEADERS.contains(key.toUpperCase())) {
                if (isDesenHeader(key.trim())) {
                    /**
                     * 对需要脱敏的头进行脱敏
                     */
                    value = desenData(value);
                }
                map.put(key, value);
            }
        }

        return map;
    }


    private void getRequestBody(HttpServletRequest request, RequestData requestData) {
        String requestBody;
        ContentCachingRequestWrapper wrapper = WebUtils.getNativeRequest(request, ContentCachingRequestWrapper.class);
        if (wrapper != null) {
            try {
                requestBody = IOUtils.toString(wrapper.getContentAsByteArray(), wrapper.getCharacterEncoding());
                requestData.setRequest(requestBody);

                Map<String, String> headerMap = getHeaderInfo(request);
                requestData.setHeader(headerMap);
            } catch (IOException e) {
                // NOOP
            }

        }
    }

    private void getResponseBody(HttpServletResponse response, RequestData requestData) {
        String responseBody;
        ContentCachingResponseWrapper wrapper = WebUtils.getNativeResponse(response, ContentCachingResponseWrapper.class);
        if (wrapper != null) {
            try {
                responseBody = IOUtils.toString(wrapper.getContentAsByteArray(), wrapper.getCharacterEncoding());
                requestData.setResponse(responseBody);
            } catch (IOException e) {
                // NOOP
            }
        }
    }

    private void updateResponse(HttpServletResponse response) throws IOException {
        TraceLogContext.clear();
        ContentCachingResponseWrapper responseWrapper = WebUtils.getNativeResponse(response, ContentCachingResponseWrapper.class);
        Objects.requireNonNull(responseWrapper).copyBodyToResponse();
    }

    @Data
    private static class HttpTraceLog {
        private String ip;
        private String userNo;
        private String path;
        private String method;
        /**
         * 请求消耗时间
         */
        private Long costTime;
        private Integer status;
        private String requestBody;
        private String responseBody;
        private String header;
    }

}
