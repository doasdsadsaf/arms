package com.my.user.controller;

import com.alibaba.csp.sentinel.annotation.SentinelResource;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.nacos.api.config.annotation.NacosValue;
import com.my.feign.OrderFeign;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

@RestController
@RequestMapping("/user")
// nacos配置动态刷新
@RefreshScope
public class UserController {
    // 注入nacos的配置
    @NacosValue("${pattern}")
    private String dateformat;

    // 注入nacos的配置
    @NacosValue("${pattern.envSharedValue}")
    public String envSharedValue;

    @Autowired
    private OrderFeign orderFeign;

    @RequestMapping("now")
    public String now() {

        return LocalDate.now().format(DateTimeFormatter.ofPattern(envSharedValue, Locale.CHINA));
    }

    @RequestMapping("show")
    //获取请求头里的数据
    public String show(@RequestHeader(value = "name", required = false) String name) {
        JSONObject json = new JSONObject();
        json.put("name", "zhang");
        return orderFeign.show(json).toJSONString();
    }


}
