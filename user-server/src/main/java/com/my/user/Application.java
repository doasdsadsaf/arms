package com.my.user;

import com.my.config.DefaultFeignConfiguration;
import com.my.feign.OrderFeign;
import com.my.user.config.EnableRshFile;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
// defaultConfiguration 把 commons 的DefaultFeignConfiguration 注入进来,否则feign整合sentinel会注入不进OrderClientFallbackFactor
@EnableFeignClients(basePackages = {"com.my.feign"},defaultConfiguration = DefaultFeignConfiguration.class)
//@EnableFeignClients(clients = OrderFeign.class,defaultConfiguration = DefaultFeignConfiguration.class)
@EnableRshFile
public class Application {
    public static void main(String[] args) {
        SpringApplication.run(Application.class,args);
        System.out.println("=========user启动成功");
    }
}
