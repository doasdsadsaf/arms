package com.my.user.controller;

import com.alibaba.nacos.api.config.annotation.NacosValue;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

@RestController
@RequestMapping("/user")
public class UserController {
    // 注入nacos的配置
    @NacosValue("${pattern.deteformat}")
    private String dateformat;

    @RequestMapping("now")
    public String now(){
        return LocalDate.now().format(DateTimeFormatter.ofPattern(dateformat, Locale.CHINA));
    }
}
