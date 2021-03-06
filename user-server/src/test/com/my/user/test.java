
package com.my.user;

import com.my.user.config.EnableRshFile;
import com.my.user.config.RabbitMQConfig;
import com.my.user.config.RshConfigBean;
import com.my.user.utils.FtpUtil;
import org.junit.jupiter.api.Test;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

@SpringBootTest
public class test {
    @Autowired
    private RabbitTemplate rabbitTemplate;
    @Autowired
    private FtpUtil ftpUtil;
    @Autowired
    private RshConfigBean rshConfigBean;

    @Test
    public void test(){
        rabbitTemplate.convertAndSend(RabbitMQConfig.ITEM_TOPIC_EXCHANGE, "item.insert", "商品新增，routing key 为item.insert");
        rabbitTemplate.convertAndSend(RabbitMQConfig.ITEM_TOPIC_EXCHANGE, "item.update", "商品修改，routing key 为item.update");
        rabbitTemplate.convertAndSend(RabbitMQConfig.ITEM_TOPIC_EXCHANGE, "item.delete", "商品删除，routing key 为item.delete");
    }

    @Test
    public void uploadFile() throws FileNotFoundException {
        String my = ftpUtil.uploadFile("my", "myaaddd.jpg", new FileInputStream(new File("C:\\Users\\PC\\Desktop\\微信图片_20220105154242.jpg")));
        System.out.println(my);
    }


}

