package com.my.user.controller;

import com.my.user.config.RabbitMQConfig;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * 发送消息的测试类
 * @创建人 dw
 * @创建时间 2021/12/28
 * @描述
 */
public class SendMsgController {
    //注入RabbitMQ的模板
    @Autowired
    private RabbitTemplate rabbitTemplate​;




    @GetMapping("/sendmsg")
    public String sendMsg(@RequestParam String msg, @RequestParam String key){
        rabbitTemplate​.receiveAndConvert();
        /**
         * 发送消息
         * 参数一：交换机名称
         * 参数二：路由key
         * 参数三：发送的消息
         */
        rabbitTemplate​.convertAndSend(RabbitMQConfig.ITEM_TOPIC_EXCHANGE ,key ,msg);
        // 返回消息
        return "发送消息成功！";
    }

    public static void main(String[] args) {
        String s = "Gone with the wind";
        String standard = s.toUpperCase();
    }

}
