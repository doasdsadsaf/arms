package com.my.user.config;

import jdk.nashorn.internal.runtime.FindProperty;
import org.springframework.amqp.core.*;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @创建人 dw
 * @创建时间 2021/12/27
 * @描述
 */
@Configuration
public class RabbitMQConfig {

    //交换机名称
    public static final String ITEM_TOPIC_EXCHANGE = "topic_exchange";

    public static final String ITEM_QUEUE = "queue";

    //声明交换机
    @Bean("itemTopicExchange")
    public Exchange topicExchange(){
        // 创建一个交换机,并持久化
        return ExchangeBuilder.topicExchange(ITEM_TOPIC_EXCHANGE).durable(true).build();}
        // 声明队列
    @Bean("itemQueue")
    public Queue itemQueue() {
        return QueueBuilder.durable(ITEM_QUEUE).build();
    }
    // 绑定队列和交换机
    @Bean
    public Binding itemQueueExchange(@Qualifier("itemQueue") Queue queue,
                                     @Qualifier("itemTopicExchange") Exchange exchange){
        return BindingBuilder.bind(queue).to(exchange).with("item.#").noargs();
    }
}
