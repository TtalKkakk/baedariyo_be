package com.house.biet.order.command.infrastructure.messaging.rabbitmq;

import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class RabbitTopologyConfig {

    @Bean
    public TopicExchange orderExchange() {
        return new TopicExchange("order.exchange");
    }

    @Bean
    public Queue riderNotificationQueue() {
        return QueueBuilder.durable("rider.notification.queue")
                .withArgument("x-message-ttl", 30000)
                .withArgument("x-dead-letter-exchange", "order.dlx")
                .build();
    }

    @Bean
    public Binding riderBinding() {
        return BindingBuilder
                .bind(riderNotificationQueue())
                .to(orderExchange())
                .with("order.created.seoul.*");
    }
}
