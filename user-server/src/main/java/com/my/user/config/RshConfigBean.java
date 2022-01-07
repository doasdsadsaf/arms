package com.my.user.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

/**
 * @创建人 dw
 * @创建时间 2020/5/27
 * @描述
 */
@Configuration
@Component
public class RshConfigBean {

    /**
     * ip
     */
    @Value("${file.host:}")
    public String host;

    /**
     * port
     */
    @Value("${file.port:}")
    public Integer port;

    /**
     * 用户名
     */
    @Value("${file.username:}")
    public String username;

    /**
     * password
     */
    @Value("${file.password:}")
    public String password;

    /**
     * 基础目录
     */
    @Value("${file.basePath:}")
    public String basePath;

    /**
     * 返回根路径
     */
    @Value("${file.returnUrl:}")
    public String returnUrl;

    @Value("${file.config:}")
    public String config;


    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public Integer getPort() {
        return port;
    }

    public void setPort(Integer port) {
        this.port = port;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getBasePath() {
        return basePath;
    }

    public void setBasePath(String basePath) {
        this.basePath = basePath;
    }

    public String getReturnUrl() {
        return returnUrl;
    }

    public void setReturnUrl(String returnUrl) {
        this.returnUrl = returnUrl;
    }

    public String getConfig() {
        return config;
    }

    public void setConfig(String config) {
        this.config = config;
    }
}
