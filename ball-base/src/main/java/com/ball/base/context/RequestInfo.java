package com.ball.base.context;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class RequestInfo {
    private String ip;
    private String uri;
    private String terminal;
    private String client;
    private String referer;
    private String lang;
}
