package com.house.biet.support.config;

import org.springframework.amqp.core.*;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;

@TestConfiguration
public class RabbitTestConfig {

    public static final String EXCHANGE_NAME = "rider.call.exchange";
    public static final String QUEUE_NAME = "rider.call.queue";
    public static final String ROUTING_KEY = "rider.call";

    @Bean
    public MessageConverter messageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    public DirectExchange riderExchange() {
        return new DirectExchange(EXCHANGE_NAME);
    }

    @Bean
    public Queue riderQueue() {
        return new Queue(QUEUE_NAME);
    }

    @Bean
    public Binding binding(Queue riderQueue, DirectExchange riderExchange) {
        return BindingBuilder.bind(riderQueue)
                .to(riderExchange)
                .with(ROUTING_KEY);
    }
}
