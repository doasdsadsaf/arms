package com.my.config;

import com.my.fallback.OrderClientFallbackFactory;
import feign.Logger;
import org.springframework.context.annotation.Bean;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

/**
 * @创建人 dw
 * @创建时间 2021/12/23
 * @描述
 */
@Component
public class DefaultFeignConfiguration {

//    @Bean
//    public Logger.Level logLevel(){
//        return Logger.Level.BASIC;
//    }

    @Bean
    public OrderClientFallbackFactory orderClientFallbackFactory(){
        return new OrderClientFallbackFactory();
    }
}
