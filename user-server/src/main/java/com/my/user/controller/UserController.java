package com.my.user.controller;

import com.alibaba.csp.sentinel.annotation.SentinelResource;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.nacos.api.config.annotation.NacosValue;
import com.my.feign.OrderFeign;
import org.apache.tomcat.util.threads.ThreadPoolExecutor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.web.bind.annotation.*;

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

    @NacosValue("${name}")
    private String name;

    @Autowired
    private OrderFeign orderFeign;

    @Autowired
    private ThreadPoolTaskExecutor threadPoolTaskExecutor;

    @RequestMapping("now")
    public String now() {
        System.out.println(name);
        return LocalDate.now().format(DateTimeFormatter.ofPattern(envSharedValue, Locale.CHINA));
    }

    @RequestMapping("show")
    //获取请求头里的数据
    public String show(@RequestHeader(value = "name", required = false) String name) {
        JSONObject json = new JSONObject();
        json.put("name", "zhang");
        return orderFeign.show(json).toJSONString();
    }



    @PostMapping
    @RequestMapping
    @ResponseBody
    public String thread(){
        threadPoolTaskExecutor.execute(()->{
            System.out.println("222");
        });
        System.out.print("11");
        ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor();
        threadPoolExecutor.execute();
        return "1";
    }

}
