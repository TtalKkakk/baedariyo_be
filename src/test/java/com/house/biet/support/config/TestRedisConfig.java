package com.house.biet.support.config;

import org.mockito.Answers;
import org.mockito.Mockito;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;

@TestConfiguration
@Profile("test")
public class TestRedisConfig {

    @Bean
    @Primary
    public RedisTemplate<String, Object> testRedisTemplate() {
        return Mockito.mock(RedisTemplate.class, Answers.RETURNS_DEEP_STUBS);
    }

    @Bean
    @Primary
    public RedisTemplate<String, String> testStringValueRedisTemplate() {
        return Mockito.mock(RedisTemplate.class, Answers.RETURNS_DEEP_STUBS);
    }

    @Bean
    public StringRedisTemplate testStringRedisTemplate() {
        return Mockito.mock(StringRedisTemplate.class, Answers.RETURNS_DEEP_STUBS);
    }
}
