package com.my.user.config;

import lombok.Data;
import org.checkerframework.checker.units.qual.A;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import javax.annotation.security.DenyAll;

/**
 * @创建人 dw
 * @创建时间 2021/12/17
 * @描述
 */
@Data
@Component
@ConfigurationProperties(prefix = "pattern")
public class PaternProperties {
    //private String dateformat

    private String envSharedValue;

}
