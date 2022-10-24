package com.ball.job.config;

/**
 * @Description:
 *
 * @Date: 2018/7/9
 * @Time: 11:39
 */
public class RedisConnectionProperties {

    /**
     * 最大空闲连接数, 默认8个
     */
    private int maxIdle;

    /**
     * 最大连接数, 默认8个
     */
    private int maxTotal;

    /**
     * 连接地址
     */
    private String host;

    /**
     * 端口
     */
    private int port;

    /**
     * 密码
     */
    private String password;

    public int getMaxIdle() {
        return maxIdle;
    }

    public void setMaxIdle(int maxIdle) {
        this.maxIdle = maxIdle;
    }


    public int getMaxTotal() {
        return maxTotal;
    }

    public void setMaxTotal(int maxTotal) {
        this.maxTotal = maxTotal;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}

