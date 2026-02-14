package com.house.biet.support.config;

import com.house.biet.order.command.infrastructure.messaging.rider.RiderCallSender;
import org.mockito.Mockito;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;

@TestConfiguration
@Profile("test")
public class TestRiderCallSenderConfig {

    @Bean
    @Primary
    public RiderCallSender riderCallSender() {
        return Mockito.mock(RiderCallSender.class);
    }
}
