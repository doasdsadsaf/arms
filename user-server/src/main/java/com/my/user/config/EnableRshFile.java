package com.my.user.config;

import com.my.user.utils.FtpUtil;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
@Import({RshConfigBean.class, FtpUtil.class})
@Configuration
public @interface EnableRshFile {

    }
